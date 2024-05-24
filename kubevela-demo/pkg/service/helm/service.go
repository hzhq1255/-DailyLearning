package helm

import (
	"context"
	"fmt"
	"helm.sh/helm/v3/pkg/action"
	"helm.sh/helm/v3/pkg/chart/loader"
	"helm.sh/helm/v3/pkg/cli"
	"helm.sh/helm/v3/pkg/release"
	"strings"
)

var _ Service = (*internalHelmService)(nil)

type internalHelmService struct {
	settings     *cli.EnvSettings
	actionConfig *action.Configuration
}

func (hms *internalHelmService) GetValues(releaseName string, opts GetValuesOptions) (map[string]interface{}, error) {
	getValues, err := opts.Build()
	if err != nil {
		return nil, fmt.Errorf("failed to build GetValues: %w", err)
	}
	values, err := getValues.Run(releaseName)
	if err != nil {
		return nil, fmt.Errorf("failed to get values: %w", err)
	}
	return values, nil
}

func (hms *internalHelmService) List(listOptions ListOptions) ([]*release.Release, error) {
	client, err := listOptions.Build()
	if err != nil {
		return nil, err
	}
	releases, err := client.Run()
	if err != nil {
		return nil, err
	}
	return releases, err
}

func (hms *internalHelmService) Status(releaseName string, opts StatusOptions) (*release.Release, error) {
	client, err := opts.Build()
	if err != nil {
		return nil, err
	}
	rel, err := client.Run(releaseName)
	if err != nil {
		return nil, err
	}
	return rel, err
}

func (hms *internalHelmService) Install(ctx context.Context, chartURI string, options InstallOption) (*release.Release, error) {
	install, err := options.Build()
	if err != nil {
		return nil, err
	}
	if strings.HasPrefix("http", chartURI) || strings.HasPrefix("https", chartURI) {
		option := PullOption{
			GlobalOption: GlobalOption{},
			Pull:         &action.Pull{},
		}
		pull, err := option.Build()
		if err != nil {
			return nil, err
		}
		_, err = pull.Run(chartURI)
	}
	chart, err := loader.Load(chartURI)
	if err != nil {
		return nil, err
	}
	return install.RunWithContext(ctx, chart, options.Values)
}

func (hms *internalHelmService) Upgrade(ctx context.Context, options UpgradeOptions) error {
	//TODO implement me
	panic("implement me")
}

func (hms *internalHelmService) Uninstall(ctx context.Context, options UninstallOptions) error {
	//TODO implement me
	panic("implement me")
}
