package kube

import (
	"reflect"
	"testing"
)

func Test_mergePolicyRules(t *testing.T) {
	type args struct {
		slices [][]rbacv1.PolicyRule
	}
	tests := []struct {
		name string
		args args
		want []rbacv1.PolicyRule
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := mergePolicyRules(tt.args.slices...); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("mergePolicyRules() = %v, want %v", got, tt.want)
			}
		})
	}
}
