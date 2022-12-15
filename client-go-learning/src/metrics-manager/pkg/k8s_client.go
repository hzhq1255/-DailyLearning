package pkg

import (
	"flag"
	"k8s.io/apimachinery/pkg/runtime/serializer"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/kubernetes/scheme"
	"k8s.io/client-go/rest"
	"k8s.io/client-go/tools/clientcmd"
	"k8s.io/client-go/util/homedir"
	metricsv1beta1 "k8s.io/metrics/pkg/apis/metrics/v1beta1"
	metricsv "k8s.io/metrics/pkg/client/clientset/versioned"
	"os"
	"path/filepath"
)

func getConfig() (config *rest.Config) {
	var kubeconfig, master string
	if host := os.Getenv("KUBERNETES_SERVICE_HOST"); host != "" {
		clusterconfig, err := rest.InClusterConfig()
		if err != nil {
			panic(err.Error())
		}
		return clusterconfig
	}
	if home := homedir.HomeDir(); home != "" {
		flag.StringVar(&kubeconfig, "kubeconfig", filepath.Join(home, ".kube", "config"), "absolute path to kubeconfig file")
	} else {
		flag.StringVar(&kubeconfig, "kubeconfig", "", "absolute path to kubeconfig file")
	}
	flag.StringVar(&master, "master", "", "master url")
	flag.Parse()
	config, err := clientcmd.BuildConfigFromFlags(master, kubeconfig)
	if err != nil {
		panic(err.Error())
	}
	return
}

func GetK8sClient() *kubernetes.Clientset {
	config := getConfig()
	clientset, err := kubernetes.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}
	return clientset
}

func GetMetricsClient() *metricsv.Clientset {

	config := getConfig()
	clienset, err := metricsv.NewForConfig(config)
	if err != nil {
		panic(err.Error())
	}
	return clienset

}

func GetCrdMetricsClient() *rest.RESTClient {
	config := getConfig()

	_ = metricsv1beta1.AddToScheme(scheme.Scheme)
	crdConfig := *config
	crdConfig.ContentConfig.GroupVersion = &metricsv1beta1.SchemeGroupVersion
	crdConfig.APIPath = "/apis"
	crdConfig.NegotiatedSerializer = serializer.NewCodecFactory(scheme.Scheme)
	crdConfig.UserAgent = rest.DefaultKubernetesUserAgent()
	c, err := rest.UnversionedRESTClientFor(&crdConfig)
	if err != nil {
		panic(err.Error())
	}
	return c
}
