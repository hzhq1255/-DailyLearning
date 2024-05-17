package kube

import (
	rbacv1 "k8s.io/api/rbac/v1"
)

// PolicyRule  copy from k8s.io/api/rbac/v1/types.go 为了解决 API group 字段不能 yaml 解析的问题
type PolicyRule struct {
	// Verbs is a list of Verbs that apply to ALL the ResourceKinds and AttributeRestrictions contained in this rule.  VerbAll represents all kinds.
	Verbs []string `json:"verbs" yaml:"verbs"`

	// APIGroups is the name of the APIGroup that contains the resources.  If multiple API groups are specified, any action requested against one of
	// the enumerated resources in any API group will be allowed.
	// +optional
	APIGroups []string `json:"apiGroups,omitempty" yaml:"apiGroups"`
	// Resources is a list of resources this rule applies to.  ResourceAll represents all resources.
	// +optional
	Resources []string `json:"resources,omitempty" yaml:"resources"`
	// ResourceNames is an optional white list of names that the rule applies to.  An empty set means that everything is allowed.
	// +optional
	ResourceNames []string `json:"resourceNames,omitempty" yaml:"resourceNames"`
}

type ScopePolicyType string

const ScopeCluster ScopePolicyType = "Cluster"
const ScopeNamespaced ScopePolicyType = "Namespaced"
const ScopeAllName string = "*"

const ScopeField = "scope"
const ScopeNamesFiled = "scopeNames"

// ScopePolicyRule kubernetes scoped policy rule
type ScopePolicyRule struct {
	// Scope Cluster, Namespaced Effect
	Scope ScopePolicyType `json:"scope,omitempty" yaml:"scope,omitempty"`
	// ScopeNames scope=Cluster scopeNames=ClusterIds
	// scope=Namespaced scopeNames=ClusterId/NamespaceName
	// if scopeNames contains "*"  all namespace or cluster
	ScopeNames []string `json:"scopeNames,omitempty" yaml:"scopeNames,omitempty"`
	// Rules yaml rules
	Rules []rbacv1.PolicyRule `json:"rules,omitempty" yaml:"rules,omitempty"`
}
