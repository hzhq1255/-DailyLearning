package helm

import (
	"context"
	"helm.sh/helm/v3/pkg/release"
)

type Service interface {
	List(options ListOptions) ([]*release.Release, error)
	GetValues(opts GetValuesOptions) (map[string]interface{}, error)
	Status(opts StatusOptions) (*release.Release, error)
	Install(ctx context.Context, chartURI string, options InstallOptions) (*release.Release, error)
	Upgrade(ctx context.Context, options UpgradeOptions) error
	Uninstall(ctx context.Context, options UninstallOptions) error
}
