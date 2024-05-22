package helm

import (
	"log"

	"helm.sh/helm/v3/pkg/action"
	"helm.sh/helm/v3/pkg/cli"
	"helm.sh/helm/v3/pkg/release"
)

var _ HelmService = (*internalHelmService)(nil)

type internalHelmService struct {
	settings     *cli.EnvSettings
	actionConfig *action.Configuration
}

func BuildSettings(kubeApiServer, kubeToken string, kubeInsecureSkipTLSVerify bool) *cli.EnvSettings {
	settings := cli.New()
	settings.KubeAPIServer = kubeApiServer
	settings.KubeToken = kubeToken
	settings.KubeInsecureSkipTLSVerify = kubeInsecureSkipTLSVerify
	return settings
}

func NewHelmService(kubeApiServer, kubeToken string) (HelmService, error) {
	settings := BuildSettings(kubeApiServer, kubeToken, false)
	actionConfig := new(action.Configuration)
	if err := actionConfig.Init(settings.RESTClientGetter(), settings.Namespace(), "secret", log.Printf); err != nil {
		return nil, err
	}

	return &internalHelmService{
		settings:     settings,
		actionConfig: actionConfig,
	}, nil

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

func (hms *internalHelmService) Get() {

}
