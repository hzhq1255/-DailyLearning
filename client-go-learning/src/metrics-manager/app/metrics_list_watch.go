package main

import (
	"context"
	"fmt"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/util/wait"
	"k8s.io/apimachinery/pkg/watch"
	"k8s.io/client-go/tools/cache"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	"metrics-manager/pkg"
	"time"
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
					nodeList, err := k8sclient.CoreV1().Nodes().List(context.TODO(), metav1.ListOptions{})
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
