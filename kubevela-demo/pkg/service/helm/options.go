package helm

import (
	"fmt"
	"helm.sh/helm/v3/pkg/action"
	"helm.sh/helm/v3/pkg/cli"
	"helm.sh/helm/v3/pkg/registry"
	"log"
	"net/http"
)

var _ GlobalOptions = (*GlobalOption)(nil)
var _ ListOptions = (*ListOption)(nil)
var _ GetValuesOptions = (*GetValuesOption)(nil)
var _ StatusOptions = (*StatusOption)(nil)
var _ PullOptions = (*PullOption)(nil)
var _ InstallOptions = (*InstallOption)(nil)
var _ UpgradeOptions = (*UpgradeOption)(nil)
var _ UninstallOptions = (*UninstallOption)(nil)

type GlobalOptions interface {
	GetSettings() *cli.EnvSettings
	SetSettings(settings *cli.EnvSettings)
	ApplyNamespace(val string)
	ApplyKubeApiServer(val string)
	ApplyKubeToken(val string)
	ApplyKubeInsecureSkipTLSVerify(val bool)
}

type GetOptions interface {
}

type GetValuesOptions interface {
	GlobalOptions
	ApplyRevision(val int)
	ApplyAll(val bool)
	Build() (*action.GetValues, error)
}

type StatusOptions interface {
	GlobalOptions
	ApplyReleaseName(val string)
	Build() (*action.Status, error)
}

type ListOptions interface {
	GlobalOptions
	ApplyAllNamespaces(val bool)
	ApplyFilter(val string)
	Build() (*action.List, error)
}

type PullOptions interface {
	GlobalOptions
	Build() (*action.Pull, error)
}

type InstallOptions interface {
	GlobalOptions
	ApplyReleaseName(val string)
	ApplyDryRun(val bool)
	ApplyForce(val bool)
	ApplyValues(val map[string]interface{})
	Build() (*action.Install, error)
}

type UpgradeOptions interface {
	GlobalOptions
	ApplyReleaseName(val string)
	ApplyInstall(val bool)
	ApplyDryRun(val bool)
	ApplyForce(val bool)
	Build() (*action.Upgrade, error)
}

type UninstallOptions interface {
	GlobalOptions
	ApplyDryRun(val bool)
	ApplyIgnoreNotFound(val bool)
	Build() (*action.Uninstall, error)
}

func NewListOptions() ListOptions {
	return &ListOption{List: &action.List{}}
}

type GlobalOption struct {
	cli.EnvSettings
}

func (g *GlobalOption) GetSettings() *cli.EnvSettings {
	return &g.EnvSettings
}

func (g *GlobalOption) SetSettings(settings *cli.EnvSettings) {
	if settings != nil {
		g.EnvSettings = *settings
	}
}

// ApplyKubeApiServer implements GlobalOptions.
func (g *GlobalOption) ApplyKubeApiServer(val string) {
	g.KubeAPIServer = val
}

// ApplyKubeInsecureSkipTLSVerify implements GlobalOptions.
func (g *GlobalOption) ApplyKubeInsecureSkipTLSVerify(val bool) {
	g.KubeInsecureSkipTLSVerify = val
}

// ApplyKubeToken implements GlobalOptions.
func (g *GlobalOption) ApplyKubeToken(val string) {
	g.KubeToken = val
}

// ApplyNamespace implements GlobalOptions.
func (g *GlobalOption) ApplyNamespace(val string) {
	g.SetNamespace(val)
}

// GetActionConfig ...
func (g *GlobalOption) GetActionConfig() (*action.Configuration, error) {
	settings := g.GetSettings()
	actionConfig := new(action.Configuration)
	if err := actionConfig.Init(settings.RESTClientGetter(), settings.Namespace(), "secret", log.Printf); err != nil {
		return nil, fmt.Errorf("failed to initialize Helm action configuration: %w", err)
	}
	return actionConfig, nil
}

type GetValuesOption struct {
	GlobalOption
	*action.GetValues
}

func (opt *GetValuesOption) ApplyRevision(val int) {
	opt.Version = val
}

func (opt *GetValuesOption) ApplyAll(val bool) {
	opt.AllValues = val
}

func (opt *GetValuesOption) Build() (*action.GetValues, error) {
	actionConfig, err := opt.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewGetValues(actionConfig)
	client.AllValues = opt.AllValues
	opt.GetValues = client
	return client, nil
}

type StatusOption struct {
	GlobalOption
	ReleaseName string
	*action.Status
}

func (s *StatusOption) ApplyReleaseName(val string) {
	s.ReleaseName = val
}

func (s *StatusOption) Build() (*action.Status, error) {
	actionConfig, err := s.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewStatus(actionConfig)
	s.Status = client
	return client, nil
}

type ListOption struct {
	GlobalOption
	*action.List
}

// ApplyAllNamespaces implements ListOptions.
func (l *ListOption) ApplyAllNamespaces(val bool) {
	l.AllNamespaces = val
}

// ApplyFilter implements ListOptions.
func (l *ListOption) ApplyFilter(val string) {
	l.Filter = val
}

// Build implements ListOptions.
func (l *ListOption) Build() (*action.List, error) {
	actionConfig, err := l.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewList(actionConfig)
	client.AllNamespaces = l.AllNamespaces
	client.Filter = l.Filter
	l.List = client
	return client, nil
}

type PullOption struct {
	GlobalOption
	*action.Pull
}

func (p *PullOption) Build() (*action.Pull, error) {
	client, err := registry.NewClient(
		registry.ClientOptHTTPClient(http.DefaultClient),
		registry.ClientOptPlainHTTP())
	if err != nil {
		return nil, err
	}
	pull := action.NewPull()
	pull.SetRegistryClient(client)
	p.Pull.InsecureSkipTLSverify = true
	p.Pull = pull
	return pull, nil
}

type InstallOption struct {
	GlobalOption
	*action.Install
	Values map[string]interface{}
}

// ApplyDryRun implements InstallOptions.
func (opt *InstallOption) ApplyDryRun(val bool) {
	opt.DryRun = val
}

// ApplyForce implements InstallOptions.
func (opt *InstallOption) ApplyForce(val bool) {
	opt.Force = val
}

// ApplyReleaseName implements InstallOptions.
func (opt *InstallOption) ApplyReleaseName(val string) {
	opt.ReleaseName = val
}

// ApplyValues implements InstallOptions.
func (opt *InstallOption) ApplyValues(val map[string]interface{}) {
	opt.Values = val
}

// Build ...
func (opt *InstallOption) Build() (*action.Install, error) {
	actionConfig, err := opt.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewInstall(actionConfig)
	client.Namespace = opt.GlobalOption.GetSettings().Namespace()
	client.Force = opt.Force
	client.DryRun = opt.DryRun
	client.ReleaseName = opt.ReleaseName
	opt.Install = client
	return client, nil
}

type UpgradeOption struct {
	GlobalOption
	ReleaseName string
	*action.Upgrade
	Values map[string]interface{}
}

func (opt *UpgradeOption) ApplyReleaseName(val string) {
	opt.ReleaseName = val
}

func (opt *UpgradeOption) ApplyInstall(val bool) {
	opt.Install = val
}

func (opt *UpgradeOption) ApplyDryRun(val bool) {
	opt.DryRun = val
}

func (opt *UpgradeOption) ApplyForce(val bool) {
	opt.Force = val
}

func (opt *UpgradeOption) Build() (*action.Upgrade, error) {
	actionConfig, err := opt.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewUpgrade(actionConfig)
	client.DryRun = opt.DryRun
	client.Force = opt.Force
	opt.Upgrade = client
	return client, nil
}

type UninstallOption struct {
	GlobalOption
	*action.Uninstall
}

// ApplyDryRun implements UninstallOptions.
func (opt *UninstallOption) ApplyDryRun(val bool) {
	opt.DryRun = val
}

// ApplyIgnoreNotFound implements UninstallOptions.
func (opt *UninstallOption) ApplyIgnoreNotFound(val bool) {
	opt.IgnoreNotFound = val
}

// Build implements UninstallOptions.
func (opt *UninstallOption) Build() (*action.Uninstall, error) {
	actionConfig, err := opt.GlobalOption.GetActionConfig()
	if err != nil {
		return nil, err
	}
	client := action.NewUninstall(actionConfig)
	client.DryRun = opt.DryRun
	client.IgnoreNotFound = opt.IgnoreNotFound
	opt.Uninstall = client
	return client, nil
}
