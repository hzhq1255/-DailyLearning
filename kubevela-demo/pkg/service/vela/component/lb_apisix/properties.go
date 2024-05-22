package lb_apisix

import (
	"fmt"

	"github.com/hzhq1255/daily-learning/kubevela-demo/pkg/service/vela"

	"k8s.io/apimachinery/pkg/runtime"
)

var _ vela.Properties = (*Properties)(nil)

type Properties struct {
	// todo
	Image string `json:"image"`
	Ports []Port `json:"ports"`
	Name  string
}

type Port struct {
	Port   int  `json:"port"`
	Expose bool `json:"expose"`
}

func (p *Properties) SetParameters(parameters *runtime.RawExtension) error {
	fmt.Printf("p.Image: %v\n", p.Image)
	return nil
}

func (p *Properties) GetParameters() *runtime.RawExtension {
	//TODO implement me
	panic("implement me")
}

func main() {

	fmt.Printf("\"aaa\": %v\n", "aaa")
}
