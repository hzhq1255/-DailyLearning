package main

import (
	"context"
	"encoding/json"
	"fmt"
	v1 "k8s.io/api/core/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/util/wait"
	"k8s.io/apimachinery/pkg/watch"
	"k8s.io/client-go/tools/cache"
	"k8s.io/klog/v2"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	"metrics-manager/pkg"
	"strconv"
	"time"
)

const (
	USAGE_ANNOTATION = "usage"
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
					node, err := k8sclient.CoreV1().Nodes().Get(context.TODO(), nodeMetrics.Name, metav1.GetOptions{})
					if err != nil {
						klog.Errorf("when set usage list node failed!")
						return
					}
					annotations := node.ObjectMeta.Annotations
					if annotations == nil {
						annotations = make(map[string]string)
					}
					usage := make(map[v1.ResourceName]string, 2)
					usage[v1.ResourceCPU] = strconv.FormatInt(nodeMetrics.Usage.Cpu().Value(), 10)
					usage[v1.ResourceMemory] = strconv.FormatInt(nodeMetrics.Usage.Memory().Value(), 10)
					usageJson, err := json.Marshal(usage)
					if err != nil {
						klog.Errorf("when json  node usage failed!")
						return
					}
					annotations[USAGE_ANNOTATION] = string(usageJson)
					newNode := node.DeepCopy()
					newNode.Annotations = annotations
					_, _ = k8sclient.CoreV1().Nodes().Update(context.TODO(), newNode, metav1.UpdateOptions{})
				}
			},
		},
	)
	c.Run(wait.NeverStop)
	s.List()
}

func main() {
	// list & watch metrics
	nodeMetricsWatch()
}
