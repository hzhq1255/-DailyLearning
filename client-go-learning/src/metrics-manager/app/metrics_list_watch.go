package main

import (
	"context"
	"encoding/json"
	"fmt"
	v1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/errors"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/types"
	"k8s.io/apimachinery/pkg/util/wait"
	"k8s.io/apimachinery/pkg/watch"
	applycorev1 "k8s.io/client-go/applyconfigurations/core/v1"
	"k8s.io/client-go/tools/cache"
	"k8s.io/klog/v2"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	"metrics-manager/pkg"
	myutil "metrics-manager/pkg/util"
	"strconv"
	"time"
)

const (
	UsageAnnotation  = "usage"
	DefaultNamespace = "default"
	UsageLabel       = "node-usage"
	KeyNodePrefix    = "node-"
)

func watchNodeMetricsSyncNodeUsageMap() {
	metricsclient, k8sclient := pkg.GetMetricsClient(), pkg.GetK8sClient()
	s, c := cache.NewInformer(&cache.ListWatch{
		ListFunc: func(options metav1.ListOptions) (runtime.Object, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().List(context.TODO(), options)
		},
		WatchFunc: func(options metav1.ListOptions) (watch.Interface, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().Watch(context.TODO(), options)
			//return watch.NewEmptyWatch(), nil
		},
		DisableChunking: false,
	}, &metricsv1beta1.NodeMetrics{},
		30*time.Second,
		cache.ResourceEventHandlerFuncs{
			UpdateFunc: func(oldObj, newObj interface{}) {
				if nodeMetrics, ok := newObj.(*metricsv1beta1.NodeMetrics); ok {
					nodeList, err := k8sclient.CoreV1().Nodes().List(context.TODO(), metav1.ListOptions{})
					if err != nil {
						return
					}

					podList, err := k8sclient.CoreV1().Pods("").List(context.TODO(), metav1.ListOptions{})
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
							//cm.Data[node.Name] = string(resourceUsageJson)
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
					oldUsageConfigMaps, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).List(context.TODO(), metav1.ListOptions{
						LabelSelector: UsageLabel,
					})
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
							_, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Update(context.TODO(), existedItem, metav1.UpdateOptions{})
							if err != nil {
								klog.Errorf("update %s node usage cm failed %v", existedItem.Name, err.Error())
							} else {
								//klog.Infof("update  exist cm %v", newCm.Data)
							}
						} else {
							tmpV := v.DeepCopy()
							_, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Create(context.TODO(), tmpV, metav1.CreateOptions{})
							if err != nil {
								klog.Errorf("create %s node usage cm failed %v", tmpV.Name, err.Error())
							} else {
								//klog.Infof("create new cm %v", newCm.Data)
							}
						}
					}
					// sync map
					for _, item := range oldUsageConfigMaps.Items {
						if _, ok := existedMap[item.Name]; !ok {
							err := k8sclient.CoreV1().ConfigMaps(item.Namespace).Delete(context.TODO(), item.Name, metav1.DeleteOptions{})
							if err != nil && !errors.IsNotFound(err) {
								klog.Errorf("delete no need %s node usage cm failed , error is %v", item.Name, err.Error())
							}
						}
					}
				}
			},
		})
	c.Run(wait.NeverStop)
	s.List()
}

func getNodeResourceUsages(node v1.Node, nodeMetrics metricsv1beta1.NodeMetrics, podList []v1.Pod) pkg.ResourceUsages {
	resourceUsages := pkg.ResourceUsages{
		CPU:              pkg.ResourceUsage{},
		Memory:           pkg.ResourceUsage{},
		EphemeralStorage: pkg.ResourceUsage{},
		PodNum:           pkg.ResourceUsage{},
	}
	allocatable := node.Status.Allocatable
	if len(allocatable) == 0 {
		allocatable = node.Status.Capacity
	}
	usage := nodeMetrics.Usage
	reqs, limits := myutil.GetPodsTotalRequestsAndLimits(&v1.PodList{Items: podList})
	resourceUsages.CPU = func() pkg.ResourceUsage {
		req, limit, alloc, usage := reqs[v1.ResourceCPU], limits[v1.ResourceCPU], allocatable[v1.ResourceCPU], usage[v1.ResourceCPU]
		return pkg.ResourceUsage{
			Reqs:        req.MilliValue(),
			Limits:      limit.MilliValue(),
			Allocatable: alloc.MilliValue(),
			Usage:       usage.MilliValue(),
		}
	}()
	resourceUsages.Memory = func() pkg.ResourceUsage {
		req, limit, alloc, usage := reqs[v1.ResourceMemory], limits[v1.ResourceMemory], allocatable[v1.ResourceMemory], usage[v1.ResourceMemory]
		return pkg.ResourceUsage{
			Reqs:        req.Value(),
			Limits:      limit.Value(),
			Allocatable: alloc.Value(),
			Usage:       usage.Value(),
		}
	}()
	resourceUsages.PodNum = func() pkg.ResourceUsage {
		return pkg.ResourceUsage{
			Allocatable: allocatable.Pods().Value(),
			Usage:       int64(len(podList)),
		}
	}()
	resourceUsages.PodNum = func() pkg.ResourceUsage {
		req, limit, alloc := reqs[v1.ResourceEphemeralStorage], limits[v1.ResourceEphemeralStorage], allocatable[v1.ResourceEphemeralStorage]
		return pkg.ResourceUsage{
			Reqs:        req.Value(),
			Limits:      limit.Value(),
			Allocatable: alloc.Value(),
			Usage:       req.Value(),
		}
	}()
	return resourceUsages

}

func nodeMetricsWatch() {
	metricsclient := pkg.GetMetricsClient()
	k8sclient := pkg.GetK8sClient()
	s, c := cache.NewInformer(&cache.ListWatch{
		ListFunc: func(options metav1.ListOptions) (runtime.Object, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().List(context.TODO(), options)
		},
		WatchFunc: func(options metav1.ListOptions) (watch.Interface, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().Watch(context.TODO(), options)
		},
		DisableChunking: true,
	},
		&metricsv1beta1.NodeMetrics{},
		60*time.Second,
		cache.ResourceEventHandlerFuncs{
			UpdateFunc: func(oldObj, newObj interface{}) {
				if nodeMetrics, ok := newObj.(*metricsv1beta1.NodeMetrics); ok {
					fmt.Printf("cpu usgae = %v, memory usage = %v, pods usage = %v storage usage = %v\n",
						nodeMetrics.Usage.Cpu(), nodeMetrics.Usage.Memory(), nodeMetrics.Usage.Pods(), nodeMetrics.Usage.StorageEphemeral())
					node, err := k8sclient.CoreV1().Nodes().Get(context.TODO(), nodeMetrics.Name, metav1.GetOptions{})
					if err != nil {
						klog.Errorf("when set usage list node failed! cased by %v", err.Error())
						return
					}
					usage := make(map[v1.ResourceName]string, 2)
					usage[v1.ResourceCPU] = strconv.FormatInt(nodeMetrics.Usage.Cpu().MilliValue(), 10)
					usage[v1.ResourceMemory] = strconv.FormatInt(nodeMetrics.Usage.Memory().Value(), 10)
					usageJson, err := json.Marshal(usage)
					if err != nil {
						klog.Errorf("when json  node usage failed! cased by %v", err.Error())
						return
					}
					patchBody := map[string]interface{}{
						"metadata": map[string]interface{}{
							"annotations": map[string]string{
								UsageAnnotation: string(usageJson),
							},
						},
					}
					patchPayload, _ := json.Marshal(patchBody)

					r, err := k8sclient.CoreV1().Nodes().Patch(context.TODO(), nodeMetrics.Name, types.StrategicMergePatchType, patchPayload, metav1.PatchOptions{})
					if err != nil {
						klog.Errorf("when update  node usage failed! cased by %v", err.Error())
					}
					klog.Infof("new node annotations %v", r.ObjectMeta.Annotations)
					allocatable := node.Status.Allocatable
					if len(allocatable) == 0 {
						allocatable = node.Status.Capacity
					}

					//cpuVal, memVal := nodeMetrics.Usage.Cpu().MilliValue(), nodeMetrics.Usage.Memory().Value()
					podList, err := k8sclient.CoreV1().Pods("").List(context.TODO(), metav1.ListOptions{
						FieldSelector: "spec.nodeName=" + nodeMetrics.Name,
					})
					filteredPodList := &v1.PodList{}
					for _, pod := range podList.Items {
						if pod.Status.Phase != v1.PodSucceeded && pod.Status.Phase != v1.PodFailed {
							filteredPodList.Items = append(filteredPodList.Items, pod)
						}
					}
					reqs, limits := myutil.GetPodsTotalRequestsAndLimits(filteredPodList)

					if err != nil {
						klog.Errorf("get pod list failed")
					}

					//usageMapJson, _ := json.Marshal(usageMap)
					nodeResourceUsages := pkg.ResourceUsages{
						CPU: func() pkg.ResourceUsage {
							req, limit := reqs[v1.ResourceCPU], limits[v1.ResourceCPU]
							return pkg.ResourceUsage{
								Reqs:        req.MilliValue(),
								Limits:      limit.MilliValue(),
								Allocatable: allocatable.Cpu().MilliValue(),
								Usage:       nodeMetrics.Usage.Cpu().MilliValue(),
							}
						}(),
						Memory: func() pkg.ResourceUsage {
							req, limit := reqs[v1.ResourceMemory], limits[v1.ResourceMemory]
							return pkg.ResourceUsage{
								Reqs:        req.Value(),
								Limits:      limit.Value(),
								Allocatable: allocatable.Memory().Value(),
								Usage:       nodeMetrics.Usage.Memory().Value(),
							}
						}(),
						EphemeralStorage: func() pkg.ResourceUsage {
							req, limit := reqs[v1.ResourceEphemeralStorage], limits[v1.ResourceEphemeralStorage]
							return pkg.ResourceUsage{
								Reqs:        req.Value(),
								Limits:      limit.Value(),
								Allocatable: allocatable.StorageEphemeral().Value(),
								Usage:       req.Value(),
							}
						}(),
						PodNum: func() pkg.ResourceUsage {
							return pkg.ResourceUsage{
								Allocatable: allocatable.Pods().Value(),
								Usage:       int64(len(podList.Items)),
							}
						}(),
					}
					_, err = applyUsageConfigMap(*nodeMetrics, *node, nodeResourceUsages)
					//syncResourceQuota(*nodeMetrics, nodeResourceUsages)
				}
			},
		},
	)
	c.Run(wait.NeverStop)
	s.List()
}

func getRequestsAndLimits() {

}

func syncResourceQuota(nodeMetrics metricsv1beta1.NodeMetrics, reosurceUsage pkg.ResourceUsages) {
	annotation := nodeMetrics.Annotations
	if annotation == nil {
		annotation = make(map[string]string)
	}
	nodeResourceUsagesJson, _ := json.Marshal(reosurceUsage)
	annotation[UsageAnnotation] = string(nodeResourceUsagesJson)
	newNodeMetrics := nodeMetrics.DeepCopy()
	newNodeMetrics.Annotations = annotation
	//metricsclient := pkg.GetMetricsClient()

}

func applyUsageConfigMap(nodeMetrics metricsv1beta1.NodeMetrics, node v1.Node, nodeResourceUsages pkg.ResourceUsages) (*pkg.ClusterResourceUsages, error) {
	usageCmName := "cluster-usage-cm"
	k8sclient := pkg.GetK8sClient()
	usageCm, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Get(context.TODO(), usageCmName, metav1.GetOptions{})
	cmNotFound := false
	if errors.IsNotFound(err) {
		cmNotFound = true
		klog.Errorf("usage cm %s not found", usageCm)
	} else if err != nil {
		klog.Errorf("get cm error %s", err)
		return nil, err
	}
	nodeResourceUsagesJson, _ := json.Marshal(nodeResourceUsages)

	if cmNotFound {
		usageCm = &v1.ConfigMap{
			TypeMeta: metav1.TypeMeta{
				APIVersion: "v1",
				Kind:       "ConfigMap",
			},
			ObjectMeta: metav1.ObjectMeta{
				Name:      usageCmName,
				Namespace: DefaultNamespace,
			},
			Data: map[string]string{
				KeyNodePrefix + nodeMetrics.Name: string(nodeResourceUsagesJson),
			},
		}
		_, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Create(context.TODO(), usageCm, metav1.CreateOptions{})
		if err != nil {
			klog.Errorf("create cluster usage map failed %v", err.Error())
			return nil, err
		}
	} else {
		data := usageCm.Data
		if data == nil {
			data = make(map[string]string)
		}
		data[KeyNodePrefix+nodeMetrics.Name] = string(nodeResourceUsagesJson)
		_, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Update(context.TODO(), usageCm, metav1.UpdateOptions{})
		if err != nil {
			klog.Errorf("update cluster usage map failed %v", err.Error())
			return nil, err
		}
	}
	return nil, nil
}

func testPatch() {
	k8sclient := pkg.GetK8sClient()
	nodeApplyCfg := applycorev1.Node("k3d-mycluster-server-0")
	nodeApplyCfg.Annotations = map[string]string{
		"a": "b",
	}
	applyJson, _ := json.Marshal(nodeApplyCfg)
	fmt.Printf("node apply %s\n", string(applyJson))
	n, _ := k8sclient.CoreV1().Nodes().Apply(context.TODO(), nodeApplyCfg, metav1.ApplyOptions{})
	nJson, _ := json.Marshal(n)
	fmt.Printf("node %s\n", string(nJson))
	patchBody := map[string]interface{}{
		"metadata": map[string]interface{}{
			"annotations": map[string]string{
				"a": "b",
			},
		},
	}
	patchJson, _ := json.Marshal(patchBody)
	n, _ = k8sclient.CoreV1().Nodes().Patch(context.TODO(), "k3d-mycluster-server-0", types.StrategicMergePatchType, patchJson, metav1.PatchOptions{})
	fmt.Printf("new patch node %s\n", string(nJson))
}

func main() {
	// list & watch metrics
	//nodeMetricsWatch()
	watchNodeMetricsSyncNodeUsageMap()

}
