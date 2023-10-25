package model

type ResourceUsage struct {
	Reqs        int64 `json:"reqs"`
	Limits      int64 `json:"limits"`
	Allocatable int64 `json:"allocatable"`
	Usage       int64 `json:"usage"`
}

type ResourceUsages struct {
	CPU              ResourceUsage `json:"cpu"`
	Memory           ResourceUsage `json:"memory"`
	EphemeralStorage ResourceUsage `json:"ephemeralStorage"`
	PodNum           ResourceUsage `json:"podNum"`
}

type ClusterResourceUsages struct {
	Cluster ResourceUsages            `json:"cluster"`
	Nodes   map[string]ResourceUsages `json:"nodes"`
}
