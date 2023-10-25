package main

import (
	"flag"
	"fmt"
	v1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/fields"
	"k8s.io/apimachinery/pkg/util/wait"
	"k8s.io/client-go/kubernetes"
	"k8s.io/client-go/kubernetes/scheme"
	"k8s.io/client-go/rest"
	"k8s.io/client-go/tools/cache"
	"k8s.io/client-go/tools/clientcmd"
	"k8s.io/client-go/util/homedir"
	"path/filepath"
	"time"
)

type PodManager struct {
}

func (p *PodManager) OnAdd(obj interface{}) {
}

func (p *PodManager) OnUpdate(oldObj, newObj interface{}) {
	if pods, ok := newObj.(*v1.Pod); ok {
		fmt.Println(pods)
	}
}

func (p *PodManager) OnDelete(obj interface{}) {
}

func initConfig() (restClient *rest.RESTClient) {
	var kubeconfig *string

	// home是家目录，如果能取得家目录的值，就可以用来做默认值
	if home := homedir.HomeDir(); home != "" {
		// 如果输入了kubeconfig参数，该参数的值就是kubeconfig文件的绝对路径，
		// 如果没有输入kubeconfig参数，就用默认路径~/.kube/config
		kubeconfig = flag.String("kubeconfig", filepath.Join(home, ".kube", "config"), "(optional) absolute path to the kubeconfig file")
	} else {
		// 如果取不到当前用户的家目录，就没办法设置kubeconfig的默认目录了，只能从入参中取
		kubeconfig = flag.String("kubeconfig", "", "absolute path to the kubeconfig file")
	}
	config, err := clientcmd.BuildConfigFromFlags("", *kubeconfig)
	// 参考path : /api/v1/namespaces/{namespace}/pods
	config.APIPath = "api"
	//// pod的group是空字符串
	config.GroupVersion = &v1.SchemeGroupVersion
	//// 指定序列化工具
	config.NegotiatedSerializer = scheme.Codecs

	// 根据配置信息构建restClient实例
	restClient, err = rest.RESTClientFor(config)

	if err != nil {
		panic(err.Error())
	}
	return
}

func main() {

	var kubeconfig, master string
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
	clientset, err := kubernetes.NewForConfig(config)

	// 保存pod结果的数据结构实例
	s, c := cache.NewInformer(cache.NewListWatchFromClient(clientset.CoreV1().RESTClient(), "pods", "default", fields.Everything()),
		&v1.Pod{},
		30*time.Second,
		&PodManager{})
	c.Run(wait.NeverStop)
	s.List()
}
