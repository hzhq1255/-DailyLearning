package lb_apisix

import (
	"github.com/hzhq1255/daily-learning/kubevela-demo/pkg/service/vela"

	velacommon "github.com/oam-dev/kubevela/apis/core.oam.dev/common"
	"k8s.io/apimachinery/pkg/runtime"
)

var _ vela.Component = (*LoadBalance)(nil)
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

func (l *LoadBalance) GetTraits() []vela.Trait {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) SetTraits(traits []vela.Trait) {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) GetParameters() *runtime.RawExtension {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) SetParameters(parameters *runtime.RawExtension) error {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) FromVelaComponent(component *velacommon.ApplicationComponent) error {
	//TODO implement me
	panic("implement me")
}

func (l *LoadBalance) ToVelaComponent() (*velacommon.ApplicationComponent, error) {
	//TODO implement me
	panic("implement me")
}

type NginxTrait struct {
	// todo
}

// FromVelaTrait implements vela.Trait.
func (n *NginxTrait) FromVelaTrait(trait *velacommon.ApplicationTrait) error {
	panic("unimplemented")
}

// GetParameters implements vela.Trait.
func (n *NginxTrait) GetParameters() *runtime.RawExtension {
	panic("unimplemented")
}

// SetParameters implements vela.Trait.
func (n *NginxTrait) SetParameters(parameters *runtime.RawExtension) error {
	panic("unimplemented")
}

// ToVelaTrait implements vela.Trait.
func (n *NginxTrait) ToVelaTrait() (*velacommon.ApplicationTrait, error) {
	panic("unimplemented")
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
