package loadbalance

import (
	"context"

	"github.com/go-logr/logr"
)

type IngressControllerHandler interface {
	Create() error
	Get() error
	List() error
	Update() error
	Delete() error
}

type InstanceDriverHandler interface {
	Install() error
	Uninstall() error
	Upgrade() error
	Get() error
	List() error
}

type IngressControllerFactory struct {
	Logger logr.Logger
}

type IngressControllerKind string

const (
	Nginx   IngressControllerKind = "nginx"
	ApiSix  IngressControllerKind = "apisix"
	Traefik IngressControllerKind = "traefik"
)

type IngressControllerInstanceDriver string

const (
	HelmDriver     IngressControllerInstanceDriver = "helm"
	KubeVelaDriver IngressControllerInstanceDriver = "kubevela"
)

func (f *IngressControllerFactory) CreateController(ctx context.Context, kind IngressControllerKind, drivers ...IngressControllerInstanceDriver) IngressControllerHandler {
	driverHandlers := make([]*InstanceDriverHandler, 0, len(drivers))
	for i := range drivers {
		switch drivers[i] {
		case HelmDriver:
			// todo
			driverHandlers = append(driverHandlers, nil)
		case KubeVelaDriver:
			// todo
			driverHandlers = append(driverHandlers, nil)
		}
	}
	switch kind {
	case Nginx:
		// todo
	case ApiSix:
		// todo
	case Traefik:
		// todo
	}
	return nil
}

func (f *IngressControllerFactory) CreateControllerWithDriverFunc(ctx context.Context, kind IngressControllerKind, drivers ...InstanceDriverHandler) IngressControllerHandler {
	return nil
}
