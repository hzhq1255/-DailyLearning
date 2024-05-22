package loadbalance

import v1 "k8s.io/apimachinery/pkg/apis/meta/v1"

type IngressControllerInstanceStatus struct {
	Phase string `json:"phase"`
}

type IngressControllerInstance struct {
	Name            string                           `json:"name"`
	Namespace       string                           `json:"namespace"`
	ClusterName     string                           `json:"clusterName"`
	CreateTimestamp v1.Time                          `json:"createTimestamp"`
	Nignx           NginxIngressControllerInstance   `json:"nginx"`
	ApiSix          ApiSixIngressControllerInstance  `json:"apisix"`
	Traefik         TraefikIngressControllerInstance `json:"traefik"`
	Status          IngressControllerInstanceStatus  `json:"status"`
}

type NginxIngressControllerInstance struct {
}

type ApiSixIngressControllerInstance struct {
}

type TraefikIngressControllerInstance struct {
}
