package main

import (
	"context"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/util/wait"
	"k8s.io/apimachinery/pkg/watch"
	"k8s.io/client-go/tools/cache"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	"metrics-manager/pkg"
	"time"
)

const (
	LockKey = "lock"
)

func watchNodeMetrics() {
	metricsclient := pkg.GetMetricsClient()
	s, c := cache.NewInformer(&cache.ListWatch{
		ListFunc: func(options metav1.ListOptions) (runtime.Object, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().List(context.TODO(), options)
		},
		WatchFunc: func(options metav1.ListOptions) (watch.Interface, error) {
			return metricsclient.MetricsV1beta1().NodeMetricses().Watch(context.TODO(), options)
		},
		DisableChunking: true,
	}, &metricsv1beta1.NodeMetrics{},
		30*time.Second,
		cache.ResourceEventHandlerFuncs{
			AddFunc: func(obj interface{}) {
			},
			UpdateFunc: updateFunc,
			DeleteFunc: func(obj interface{}) {
			},
		})
	c.Run(wait.NeverStop)
	s.List()
}

func updateFunc(oldObj, newObj interface{}) {
	if nodeMetrics, ok := newObj.(*metricsv1beta1.NodeMetrics); ok {
		annotation := nodeMetrics.Annotations
		lock := false
		if _, ok := annotation[LockKey]; ok {
			if ok {
				lock = true
			}
		}
		if lock == true {
			return
		}
	}
}

func main() {
}
