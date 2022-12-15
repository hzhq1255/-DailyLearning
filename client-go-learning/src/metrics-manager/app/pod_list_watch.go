package pkg

import (
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/rest"
	"k8s.io/client-go/tools/cache"
	"k8s.io/client-go/tools/clientcmd"
)

type PodManager struct {
}

func (p *PodManager) OnAdd(obj interface{}) {
}

func (p *PodManager) OnUpdate(oldObj, newObj interface{}) {

}

func (p *PodManager) OnDelete(obj interface{}) {
}

func main() {
	var config, err = clientcmd.BuildConfigFromFlags("", clientcmd.RecommendedHomeFile)
	if err != nil {
		clusterconfig, err := rest.InClusterConfig()
		if err != nil {
			panic(err)
		}
		config = clusterconfig
	}
	clientset, err := kubernetes.NewForConfig(config)
	s, c := cache.NewInformer(cache.NewListWatchFromClient())
}
