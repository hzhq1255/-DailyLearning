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
	"strconv"
	"time"
)

const (
	UsageAnnotation  = "usage"
	DefaultNamespace = "default"
)

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
					//_, err := k8sclient.CoreV1().Nodes().Get(context.TODO(), nodeMetrics.Name, metav1.GetOptions{})
					//if err != nil {
					//	klog.Errorf("when set usage list node failed! cased by %v", err.Error())
					//	return
					//}
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
					//nodeApplyCfg := applycorev1.NodeApplyConfiguration{
					//	ObjectMetaApplyConfiguration: &applymetav1.ObjectMetaApplyConfiguration{
					//		Annotations: map[string]string{
					//			USAGE_ANNOTATION: string(usageJson),
					//		},
					//		Name: &nodeMetrics.Name,
					//	},
					//}

					r, err := k8sclient.CoreV1().Nodes().Patch(context.TODO(), nodeMetrics.Name, types.StrategicMergePatchType, patchPayload, metav1.PatchOptions{})
					if err != nil {
						klog.Errorf("when update  node usage failed! cased by %v", err.Error())
					}
					klog.Infof("new node annotations %v", r.ObjectMeta.Annotations)

					_, err = applyUsageConfigMap(*nodeMetrics)
				}
			},
		},
	)
	c.Run(wait.NeverStop)
	s.List()
}

func applyUsageConfigMap(nodeMetrics metricsv1beta1.NodeMetrics) (*pkg.ClusterResourceUsages, error) {
	k8sclient := pkg.GetK8sClient()
	usageCmName := "cluster-usage-cm"
	usageCm, err := k8sclient.CoreV1().ConfigMaps(DefaultNamespace).Get(context.TODO(), usageCmName, metav1.GetOptions{})
	cmNotFound := false
	if errors.IsNotFound(err) {
		cmNotFound = true
		klog.Errorf("usage cm %s not found", usageCm)
	} else if err != nil {
		klog.Errorf("get cm error %s", err)
		return nil, err
	}
	cpuVal, memVal := nodeMetrics.Usage.Cpu().MilliValue(), nodeMetrics.Usage.Memory().Value()
	usageMap := map[v1.ResourceName]string{
		v1.ResourceCPU:    strconv.FormatInt(cpuVal, 10),
		v1.ResourceMemory: strconv.FormatInt(memVal, 10),
	}
	usageMapJson, _ := json.Marshal(usageMap)
	if cmNotFound {
		usageCm = &v1.ConfigMap{
			TypeMeta: metav1.TypeMeta{
				APIVersion: "v1",
				Kind:       "ConfigMap",
			},
			ObjectMeta: metav1.ObjectMeta{},
			Immutable:  nil,
			Data: map[string]string{
				"node" + nodeMetrics.Name: string(usageMapJson),
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
		data["node"+nodeMetrics.Name] = string(usageMapJson)
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
	nodeMetricsWatch()

}
