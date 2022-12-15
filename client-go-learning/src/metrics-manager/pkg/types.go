package pkg

import (
	"context"
	"k8s.io/apimachinery/pkg/api/errors"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/labels"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/watch"
	informerV1 "k8s.io/client-go/informers/core/v1"
	"k8s.io/client-go/informers/internalinterfaces"
	"k8s.io/client-go/kubernetes"
	v1 "k8s.io/client-go/listers/core/v1"
	"k8s.io/client-go/tools/cache"
	"k8s.io/client-go/util/workqueue"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	metricsv "k8s.io/metrics/pkg/client/clientset/versioned"
	"time"
)

// Controller
// define resource controller
type Controller struct {
	client        kubernetes.Interface
	metricsLister NodeMetricsLister
	nodeLister    v1.NodeLister
	queue         workqueue.RateLimitingInterface
}

func NewController(client kubernetes.Interface, nodeMetricsInformer informerV1.NodeInformer) {

}

// NodeMetricsLister helps list NodeMetrics
// reference from node
type NodeMetricsLister interface {
	List(selector labels.Selector) (ret []*metricsv1beta1.NodeMetrics, err error)
	GET(name string) (*metricsv1beta1.NodeMetrics, error)
	NodeMetricsListerExpansion
}

type NodeMetricsListerExpansion interface{}

// nodeMetricsLister implement NodeMetricsLister
type nodeMetricsLister struct {
	indexer cache.Indexer
}

func (n nodeMetricsLister) List(selector labels.Selector) (ret []*metricsv1beta1.NodeMetrics, err error) {
	err = cache.ListAll(n.indexer, selector, func(m interface{}) {
		ret = append(ret, m.(*metricsv1beta1.NodeMetrics))
	})
	return ret, err
}

func (n nodeMetricsLister) GET(name string) (*metricsv1beta1.NodeMetrics, error) {
	obj, exists, err := n.indexer.GetByKey(name)
	if err != nil {
		return nil, err
	}
	if !exists {
		return nil, errors.NewNotFound(metricsv1beta1.Resource("node metrics"), name)
	}
	return obj.(*metricsv1beta1.NodeMetrics), nil
}

// NewNodeMetricsLister returns a new NodeMetricsLister.
func NewNodeMetricsLister(indexer cache.Indexer) NodeMetricsLister {
	return &nodeMetricsLister{indexer: indexer}
}

// NodeInformer provides access to a shared informer and lister for
// Nodes.
type NodeMetricsInformer interface {
	Informer() cache.SharedIndexInformer
	Lister() v1.NodeLister
}

type nodeMetricsInformer struct {
	factory          internalinterfaces.SharedInformerFactory
	tweakListOptions internalinterfaces.TweakListOptionsFunc
}

func NewNodeMetricsInformer(client metricsv.Interface, resyncPeriod time.Duration, indexers cache.Indexers) cache.SharedIndexInformer {
	return NewFilteredNodeMetricsInformer(client, resyncPeriod, indexers, nil)
}

func NewFilteredNodeMetricsInformer(client metricsv.Interface, resyncPeriod time.Duration, indexers cache.Indexers, tweakListOptions internalinterfaces.TweakListOptionsFunc) cache.SharedIndexInformer {
	return cache.NewSharedIndexInformer(
		&cache.ListWatch{
			ListFunc: func(options metav1.ListOptions) (runtime.Object, error) {
				if tweakListOptions != nil {
					tweakListOptions(&options)
				}
				return client.MetricsV1beta1().NodeMetricses().List(context.TODO(), metav1.ListOptions{})
			},
			WatchFunc: func(options metav1.ListOptions) (watch.Interface, error) {
				if tweakListOptions != nil {
					tweakListOptions(&options)
				}
				return client.MetricsV1beta1().NodeMetricses().Watch(context.TODO(), metav1.ListOptions{})
			},
		},
		&metricsv1beta1.NodeMetricsList{},
		resyncPeriod,
		indexers,
	)
}

func (f *nodeMetricsInformer) defaultInformer(client metricsv.Interface, resyncPeriod time.Duration) cache.SharedIndexInformer {
	return NewFilteredNodeMetricsInformer(client, resyncPeriod, cache.Indexers{cache.NamespaceIndex: cache.MetaNamespaceIndexFunc}, f.tweakListOptions)
}

func (f *nodeMetricsInformer) Informer() cache.SharedIndexInformer {
	return f.factory.InformerFor(&metricsv1beta1.NodeMetrics{}, nil)
}

func (f *nodeMetricsInformer) Lister() v1.NodeLister {
	return v1.NewNodeLister(f.Informer().GetIndexer())
}
