package kube

import (
	"fmt"
	"k8s.io/apimachinery/pkg/runtime/schema"
)

func ListApiResources() {
	// 使用Discovery客户端来获取支持的API资源
	discoveryClient := GetK8sClient().Discovery()

	// 获取API资源列表
	apiResources, err := discoveryClient.ServerPreferredResources()
	if err != nil {
		fmt.Printf("Error getting API resources: %v\n", err)
		return
	}

	//v1List, _ := discoveryClient.ServerResourcesForGroupVersion("v1")
	//fmt.Println(v1List)
	//
	//groupList, grList, err := discoveryClient.ServerGroupsAndResources()
	//fmt.Println(groupList, grList)
	var gvs []schema.GroupVersion
	cnt := 0
	// 打印API资源信息
	for _, apiResourceList := range apiResources {
		gv, _ := schema.ParseGroupVersion(apiResourceList.GroupVersion)
		gvs = append(gvs, gv)
	}
	for _, gv := range gvs {
		apiResourceList, _ := discoveryClient.ServerResourcesForGroupVersion(gv.String())
		for _, apiResource := range apiResourceList.APIResources {
			cnt++
			fmt.Printf("Name: %s\n", apiResource.Name)
			fmt.Printf("Namespaced: %v\n", apiResource.Namespaced)
			fmt.Printf("Kind: %s\n", apiResource.Kind)
			fmt.Printf("Verbs: %v\n", apiResource.Verbs)
			fmt.Printf("Group: %s\n", gv.Group)
			fmt.Printf("Version: %s\n\n", gv.Version)

		}
	}
	fmt.Printf("total count: %d", cnt)
}
