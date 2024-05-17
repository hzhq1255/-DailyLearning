package kube

import (
	"context"
	"encoding/json"
	"fmt"
	rbacv1 "k8s.io/api/rbac/v1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime/schema"
	"sigs.k8s.io/yaml"
	"testing"
)

func Test_roles(t *testing.T) {
	client := GetK8sClient()
	roleList, err := client.RbacV1().Roles("").List(context.TODO(), metav1.ListOptions{})
	if err != nil {
		return
	}
	var rules []rbacv1.PolicyRule
	for _, item := range roleList.Items {
		rules = append(rules, item.Rules...)
	}
	got := mergePolicyRules(rules)
	yamlData, _ := yaml.Marshal(got)
	fmt.Printf("%s\n", yamlData)
}

func Test_mergePolicyRules(t *testing.T) {
	type args struct {
		slices [][]rbacv1.PolicyRule
	}
	tests := []struct {
		name string
		args args
		want []rbacv1.PolicyRule
	}{
		{
			name: "same group different resources",
			args: args{
				slices: [][]rbacv1.PolicyRule{
					{
						{
							Verbs:         []string{"get", "list", "watch"},
							APIGroups:     []string{"apps"},
							Resources:     []string{"deployments"},
							ResourceNames: nil,
						},
						{
							Verbs:         []string{"get", "list", "watch", "create"},
							APIGroups:     []string{"apps"},
							Resources:     []string{"statefulsets", "jobs"},
							ResourceNames: nil,
						},
						{
							Verbs:         []string{"get", "list", "watch", "create"},
							APIGroups:     []string{"apps"},
							Resources:     []string{"deployments"},
							ResourceNames: nil,
						},
					},
					{
						{
							Verbs:         []string{"get", "list", "watch", "create"},
							APIGroups:     []string{"apps"},
							Resources:     []string{"deployments"},
							ResourceNames: nil,
						},
						{
							Verbs:         []string{"get", "list", "watch", "create", "update"},
							APIGroups:     []string{"apps"},
							Resources:     []string{"deployments"},
							ResourceNames: []string{"test"},
						},
						{
							Verbs:         []string{"*"},
							APIGroups:     []string{"*"},
							Resources:     []string{"*"},
							ResourceNames: []string{"*"},
						},
					},
				},
			},
			want: []rbacv1.PolicyRule{
				{
					Verbs:         []string{"get", "list", "watch"},
					APIGroups:     []string{"apps"},
					Resources:     []string{"deployments"},
					ResourceNames: nil,
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got := mergePolicyRules(tt.args.slices...)
			yamlData, _ := yaml.Marshal(got)
			fmt.Printf("%s\n", yamlData)
			//if got := mergePolicyRules(tt.args.slices...); !reflect.DeepEqual(got, tt.want) {
			//	t.Errorf("mergePolicyRules() = %v, want %v", got, tt.want)
			//}
		})
	}
}

// InstanceInfo ...
type InstanceInfo struct {
	ZoneID         string
	SubnetID       string
	InstanceTypeID string
	ChargeType     string
}

// InstanceTypeInfo ...
type InstanceTypeInfo struct {
	GPUName               string
	ENINumQuota           int32
	PrivateIPQuota        int32
	RDMANetworkInterfaces int32
	VolumeTypes           []string
}

func Test_test(t *testing.T) {
	yamlStr := `
- apiGroups:
  - "*"
  resources:
  - "*"
  verbs:
  - "*"`
	rule, _ := StringToScopeRoleRule(yamlStr, ScopeCluster)
	jsonData, _ := json.Marshal(rule)
	fmt.Println(string(jsonData))

	fmt.Println(string(jsonData))

}

func setDefaultResources(rules []*rbacv1.PolicyRule) {
	for _, rule := range rules {
		rule.ResourceNames = []string{"test-pod"}
	}
}

func Test_Change_Slice(t *testing.T) {
	gvk := schema.GroupVersionKind{
		Group:   "apps",
		Version: "v1",
		Kind:    "Deployment",
	}
	fmt.Println()
}
