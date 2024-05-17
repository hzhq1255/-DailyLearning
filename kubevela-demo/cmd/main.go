package main

import (
	"fmt"
	_ "github.com/oam-dev/kubevela/apis/core.oam.dev/v1alpha1"
	_ "k8s.io/client-go/tools/clientcmd"
)

func main() {
	fmt.Println("hello world!")
}
