package helm

import "helm.sh/helm/v3/pkg/release"

type HelmService interface {
	List(listOptions ListOptions) ([]*release.Release, error)
	Get()
}
