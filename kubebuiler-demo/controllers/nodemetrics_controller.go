/*
Copyright 2022.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controllers

import (
	"context"
	"encoding/json"
	"fmt"
	v1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/errors"
	"k8s.io/apimachinery/pkg/api/resource"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/labels"
	"k8s.io/klog/v2"

	"k8s.io/apimachinery/pkg/runtime"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"
	ctrlclient "sigs.k8s.io/controller-runtime/pkg/client"
	"sigs.k8s.io/controller-runtime/pkg/log"
)

const (
	UsageAnnotation  = "usage"
	DefaultNamespace = "default"
	UsageLabel       = "node-usage"
	KeyNodePrefix    = "node-"
)

// NodeMetricsReconciler reconciles a NodeMetrics object
type NodeMetricsReconciler struct {
	client.Client
	Scheme *runtime.Scheme
}

//+kubebuilder:rbac:groups=metrics.k8s.io,resources=nodes,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=metrics.k8s.io,resources=nodes/status,verbs=get;update;patch
//+kubebuilder:rbac:groups=metrics.k8s.io,resources=nodes/finalizers,verbs=update
//+kubebuilder:rbac:groups="",resources=configmaps,verbs=create;update;delete
//+kubebuilder:rbac:groups="",resources=nodes,verbs=get;list;watch
//+kubebuilder:rbac:groups="",resources=pods,verbs=get;list;watch

// Reconcile is part of the main kubernetes reconciliation loop which aims to
// move the current state of the cluster closer to the desired state.
// TODO(user): Modify the Reconcile function to compare the state specified by
// the NodeMetrics object against the actual cluster state, and then
// perform operations to make the cluster state reflect the state specified by
// the user.
//
// For more details, check Reconcile and its Result here:
// - https://pkg.go.dev/sigs.k8s.io/controller-runtime@v0.13.0/pkg/reconcile
func (r *NodeMetricsReconciler) Reconcile(ctx context.Context, req ctrl.Request) (ctrl.Result, error) {
	_ = log.FromContext(ctx)
	// TODO(user): your logic here
	//klog.Infof("req %v", req)
	nodeMetrics := &metricsv1beta1.NodeMetrics{}
	if err := r.Client.Get(ctx, req.NamespacedName, nodeMetrics); err != nil {
		klog.Error(err, "list node metrics failed")
		return ctrl.Result{}, err
	}
	nodeList := &v1.NodeList{}
	if err := r.Client.List(ctx, nodeList); err != nil {
		klog.Error(err, "list nodes failed")
		return ctrl.Result{}, err
	}
	podList := &v1.PodList{}
	if err := r.Client.List(ctx, podList); err != nil {
		klog.Error(err, "list pods failed")
		return ctrl.Result{}, err
	}
	podMap := map[string][]v1.Pod{}
	for _, pod := range podList.Items {
		if pod.Spec.NodeName != "" && pod.Status.Phase != v1.PodSucceeded && pod.Status.Phase != v1.PodFailed {
			l := podMap[pod.Spec.NodeName]
			podMap[pod.Spec.NodeName] = append(l, pod)
		}
	}
	nodeLen := len(nodeList.Items)
	usageConfigMapList := make([]v1.ConfigMap, 0)
	i, k, splitLen, data := 0, 0, 2000, make(map[string]string)
	for i < nodeLen {
		node := nodeList.Items[i]
		nodePodList := podMap[node.Name]
		resourceUsage := getNodeResourceUsages(node, *nodeMetrics, nodePodList)
		resourceUsageJson, _ := json.Marshal(resourceUsage)
		data[node.Name] = string(resourceUsageJson)
		if i%splitLen == splitLen-1 || i == nodeLen-1 {
			cm := v1.ConfigMap{
				ObjectMeta: metav1.ObjectMeta{
					Labels: map[string]string{
						UsageLabel: "",
					},
					Name:      "resource-usage-" + fmt.Sprintf("%d", k),
					Namespace: DefaultNamespace,
				},
				Data: data,
			}
			usageConfigMapList = append(usageConfigMapList, cm)
			data = make(map[string]string)
			k++
		}

		i++
	}
	oldUsageConfigMaps := &v1.ConfigMapList{}
	if err := r.Client.List(ctx, oldUsageConfigMaps, &ctrlclient.ListOptions{
		LabelSelector: labels.SelectorFromSet(map[string]string{UsageLabel: ""})}); err != nil {
		klog.Error(err, "list old cm failed")
		return ctrl.Result{}, err
	}
	existedMap := map[string]bool{}
	for _, v := range usageConfigMapList {
		var existedItem *v1.ConfigMap
		for _, item := range oldUsageConfigMaps.Items {
			if v.Name == item.Name {
				existedItem = item.DeepCopy()
				existedMap[v.Name] = true
				break
			}
		}
		if existedItem != nil {
			existedItem.Data = v.Data
			if err := r.Client.Update(ctx, existedItem.DeepCopy()); err != nil {
				klog.Errorf("update %s node usage cm failed %v", existedItem.Name, err.Error())
				return ctrl.Result{}, err

			}
		} else {
			tmpV := v.DeepCopy()
			if err := r.Client.Create(ctx, tmpV); err != nil {
				klog.Errorf("create %s node usage cm failed %v", tmpV.Name, err.Error())
				return ctrl.Result{}, err
			}
		}
	}
	// sync map
	for _, item := range oldUsageConfigMaps.Items {
		if _, ok := existedMap[item.Name]; !ok {
			delItem := &item
			if err := r.Client.Delete(ctx, delItem); err != nil && !errors.IsNotFound(err) {
				klog.Errorf("delete no need %s node usage cm failed , error is %v", item.Name, err.Error())
				return ctrl.Result{}, err
			}
		}
	}
	return ctrl.Result{}, nil
}

// SetupWithManager sets up the controller with the Manager.
func (r *NodeMetricsReconciler) SetupWithManager(mgr ctrl.Manager) error {
	return ctrl.NewControllerManagedBy(mgr).
		For(&metricsv1beta1.NodeMetrics{}).
		Complete(r)
}

func getNodeResourceUsages(node v1.Node, nodeMetrics metricsv1beta1.NodeMetrics, podList []v1.Pod) ResourceUsages {
	resourceUsages := ResourceUsages{
		CPU:              ResourceUsage{},
		Memory:           ResourceUsage{},
		EphemeralStorage: ResourceUsage{},
		PodNum:           ResourceUsage{},
	}
	allocatable := node.Status.Allocatable
	if len(allocatable) == 0 {
		allocatable = node.Status.Capacity
	}
	usage := nodeMetrics.Usage
	reqs, limits := GetPodsTotalRequestsAndLimits(&v1.PodList{Items: podList})
	resourceUsages.CPU = func() ResourceUsage {
		req, limit, alloc, usage := reqs[v1.ResourceCPU], limits[v1.ResourceCPU], allocatable[v1.ResourceCPU], usage[v1.ResourceCPU]
		return ResourceUsage{
			Reqs:        req.MilliValue(),
			Limits:      limit.MilliValue(),
			Allocatable: alloc.MilliValue(),
			Usage:       usage.MilliValue(),
		}
	}()
	resourceUsages.Memory = func() ResourceUsage {
		req, limit, alloc, usage := reqs[v1.ResourceMemory], limits[v1.ResourceMemory], allocatable[v1.ResourceMemory], usage[v1.ResourceMemory]
		return ResourceUsage{
			Reqs:        req.Value(),
			Limits:      limit.Value(),
			Allocatable: alloc.Value(),
			Usage:       usage.Value(),
		}
	}()
	resourceUsages.PodNum = func() ResourceUsage {
		return ResourceUsage{
			Allocatable: allocatable.Pods().Value(),
			Usage:       int64(len(podList)),
		}
	}()
	resourceUsages.PodNum = func() ResourceUsage {
		req, limit, alloc := reqs[v1.ResourceEphemeralStorage], limits[v1.ResourceEphemeralStorage], allocatable[v1.ResourceEphemeralStorage]
		return ResourceUsage{
			Reqs:        req.Value(),
			Limits:      limit.Value(),
			Allocatable: alloc.Value(),
			Usage:       req.Value(),
		}
	}()
	return resourceUsages

}

type ResourceUsage struct {
	Reqs        int64 `json:"reqs"`
	Limits      int64 `json:"limits"`
	Allocatable int64 `json:"allocatable"`
	Usage       int64 `json:"usage"`
}

type ResourceUsages struct {
	CPU              ResourceUsage `json:"cpu"`
	Memory           ResourceUsage `json:"memory"`
	EphemeralStorage ResourceUsage `json:"ephemeralStorage"`
	PodNum           ResourceUsage `json:"podNum"`
}

// PodRequestsAndLimits returns a dictionary of all defined resources summed up for all
// containers of the pod. If pod overhead is non-nil, the pod overhead is added to the
// total container resource requests and to the total container limits which have a
// non-zero quantity.
func PodRequestsAndLimits(pod *v1.Pod) (reqs, limits v1.ResourceList) {
	reqs, limits = v1.ResourceList{}, v1.ResourceList{}
	for _, container := range pod.Spec.Containers {
		addResourceList(reqs, container.Resources.Requests)
		addResourceList(limits, container.Resources.Limits)
	}
	// init containers define the minimum of any resource
	for _, container := range pod.Spec.InitContainers {
		maxResourceList(reqs, container.Resources.Requests)
		maxResourceList(limits, container.Resources.Limits)
	}

	// Add overhead for running a pod to the sum of requests and to non-zero limits:
	if pod.Spec.Overhead != nil {
		addResourceList(reqs, pod.Spec.Overhead)

		for name, quantity := range pod.Spec.Overhead {
			if value, ok := limits[name]; ok && !value.IsZero() {
				value.Add(quantity)
				limits[name] = value
			}
		}
	}
	return
}

// addResourceList adds the resources in newList to list
func addResourceList(list, new v1.ResourceList) {
	for name, quantity := range new {
		if value, ok := list[name]; !ok {
			list[name] = quantity.DeepCopy()
		} else {
			value.Add(quantity)
			list[name] = value
		}
	}
}

// maxResourceList sets list to the greater of list/newList for every resource
// either list
func maxResourceList(list, new v1.ResourceList) {
	for name, quantity := range new {
		if value, ok := list[name]; !ok {
			list[name] = quantity.DeepCopy()
			continue
		} else {
			if quantity.Cmp(value) > 0 {
				list[name] = quantity.DeepCopy()
			}
		}
	}
}

func GetPodsTotalRequestsAndLimits(podList *v1.PodList) (reqs map[v1.ResourceName]resource.Quantity, limits map[v1.ResourceName]resource.Quantity) {
	reqs, limits = map[v1.ResourceName]resource.Quantity{}, map[v1.ResourceName]resource.Quantity{}
	for _, pod := range podList.Items {
		podReqs, podLimits := PodRequestsAndLimits(&pod)
		for podReqName, podReqValue := range podReqs {
			if value, ok := reqs[podReqName]; !ok {
				reqs[podReqName] = podReqValue.DeepCopy()
			} else {
				value.Add(podReqValue)
				reqs[podReqName] = value
			}
		}
		for podLimitName, podLimitValue := range podLimits {
			if value, ok := limits[podLimitName]; !ok {
				limits[podLimitName] = podLimitValue.DeepCopy()
			} else {
				value.Add(podLimitValue)
				limits[podLimitName] = value
			}
		}
	}
	return
}
