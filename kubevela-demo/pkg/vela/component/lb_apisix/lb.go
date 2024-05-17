package lb_apisix

import (
	"github.com/hzhq1255/daily-learning/kubevela-demo/pkg/vela"
	"k8s.io/apimachinery/pkg/runtime"
)

var _ vela.Component = (*LoadBalance)(nil)
var _ vela.Properties = (*Properties)(nil)
var _ vela.Trait = (*NginxTrait)(nil)

// LoadBalance ...
type LoadBalance struct {
	Name       string          `json:"name"`
	Type       string          `json:"type"`
	Properties vela.Properties `json:"properties"`
	Traits     []vela.Trait    `json:"traits"`
}

func (l *LoadBalance) GetName() string {
	return l.Name
}

func (l *LoadBalance) SetName(name string) {
	l.Name = name
}

func (l *LoadBalance) GetType() string {
	return l.Type
}

func (l *LoadBalance) SetType(_type string) {
	l.Type = _type
}

func (l *LoadBalance) GetProperties() vela.Properties {
	return l.Properties
}

func (l *LoadBalance) SetProperties(properties vela.Properties) {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) GetTraits() []vela.Trait {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) SetTraits(traits []vela.Trait) {
	//TODO implement me
	panic("implement me")
}

type Properties struct {
	// todo
	Image string `json:"image"`
	Ports []struct {
		Port   int  `json:"port"`
		Expose bool `json:"expose"`
	} `json:"ports"`
}

func (p *Properties) GetParameters() *runtime.RawExtension {
	//TODO implement me
	panic("implement me")
}

type NginxTrait struct {
	// todo
}

func (n NginxTrait) GetType() string {
	//TODO implement me
	panic("implement me")
}

func (n NginxTrait) SetType(_type string) {
	//TODO implement me
	panic("implement me")
}

func (n NginxTrait) GetProperties() vela.Properties {
	//TODO implement me
	panic("implement me")
}

func (n NginxTrait) SetProperties(properties vela.Properties) {
	//TODO implement me
	panic("implement me")
}
