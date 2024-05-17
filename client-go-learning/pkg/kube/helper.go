package kube

import (
	"bytes"
	"encoding/json"
	"fmt"
	rbacv1 "k8s.io/api/rbac/v1"
	"k8s.io/apimachinery/pkg/util/sets"
	"sigs.k8s.io/yaml"
	"sort"
	"strings"
)

func appendPolicyRules(slices ...[]rbacv1.PolicyRule) []rbacv1.PolicyRule {
	// apps/get,list,watch -> resource -> resourceNames
	m := map[string]map[string]sets.String{}
	for _, slice := range slices {
		for _, rule := range slice {
			for _, group := range rule.APIGroups {
				sort.Strings(rule.Verbs)
				key := fmt.Sprintf("%s/%s", group, strings.Join(rule.Verbs, ","))
				if m[key] == nil {
					m[key] = map[string]sets.String{}
				}
				for _, resource := range rule.Resources {
					if m[key][resource] == nil {
						m[key][resource] = sets.NewString()
					}
					for _, resourceName := range rule.ResourceNames {
						m[key][resource].Insert(resourceName)
					}
				}
			}
		}
	}

	var rules []rbacv1.PolicyRule
	for k, v := range m {
		if len(v) == 0 {
			continue
		}
		groupAndVerbs := strings.Split(k, "/")
		group := groupAndVerbs[0]
		verbs := strings.Split(groupAndVerbs[1], ",")
		var resourceRule rbacv1.PolicyRule
		resourceRule.APIGroups = []string{group}
		resourceRule.Verbs = verbs
		var resourceNameRules []rbacv1.PolicyRule
		for resource, resourceNames := range v {
			if len(resourceNames) == 0 {
				resourceRule.Resources = append(resourceRule.Resources, resource)
			} else {
				var resourceNameRule rbacv1.PolicyRule
				resourceNameRule.APIGroups = []string{group}
				resourceNameRule.Verbs = verbs
				resourceNameRule.Resources = []string{resource}
				resourceNameRule.ResourceNames = resourceNames.List()
				resourceNameRules = append(resourceNameRules, resourceNameRule)
			}
		}
		rules = append(rules, append(resourceNameRules, resourceRule)...)
	}
	return rules
}

// mergePolicyRules merge policy rules
func mergePolicyRules(slices ...[]rbacv1.PolicyRule) []rbacv1.PolicyRule {
	// apiGroups -> resources -> resourceNames -> verbs
	m := map[string]map[string]map[string]sets.String{}
	for _, slice := range slices {
		for _, rule := range slice {
			for _, apiGroup := range rule.APIGroups {
				if m[apiGroup] == nil {
					m[apiGroup] = map[string]map[string]sets.String{}
				}

				for _, resource := range rule.Resources {
					if m[apiGroup][resource] == nil {
						m[apiGroup][resource] = map[string]sets.String{}
					}
					addVerbsFunc := func(key string) {
						if m[apiGroup][resource][key] == nil {
							m[apiGroup][resource][key] = sets.NewString(rule.Verbs...)
						}
						m[apiGroup][resource][key].Insert(rule.Verbs...)
					}
					if len(rule.ResourceNames) > 0 {
						for _, resourceName := range rule.ResourceNames {
							addVerbsFunc(resourceName)
						}
					} else {
						addVerbsFunc("*")
					}
				}
			}
		}
	}
	var rules []rbacv1.PolicyRule
	for group, resourceMap := range m {
		// verbs List -> resources
		verbsGroupMap := map[string][]string{}
		for resource, resourceNameMap := range resourceMap {
			// verbs List -> resourceNames
			verbsResourceMap := map[string][]string{}
			for resourceName, verbs := range resourceNameMap {
				verbKey := strings.Join(verbs.List(), ",")
				if resourceName == "*" {
					verbsGroupMap[verbKey] = append(verbsGroupMap[verbKey], resource)
				} else {
					verbsResourceMap[verbKey] = append(verbsResourceMap[verbKey], resourceName)
				}
			}
			for verbStr, resourceNames := range verbsResourceMap {
				rules = append(rules, rbacv1.PolicyRule{
					Verbs:         strings.Split(verbStr, ","),
					APIGroups:     []string{group},
					Resources:     []string{resource},
					ResourceNames: resourceNames,
				})
			}
		}
		for verbStr, resources := range verbsGroupMap {
			rules = append(rules, rbacv1.PolicyRule{
				Verbs:     strings.Split(verbStr, ","),
				APIGroups: []string{group},
				Resources: resources,
			})
		}
	}
	return rules
}

func StringToScopeRoleRule(s string, scopeType ScopePolicyType) ([]ScopePolicyRule, error) {
	if len(s) == 0 {
		return nil, fmt.Errorf("rule is empty")
	}
	var scopeRules []ScopePolicyRule
	var obj interface{}
	if err := yaml.Unmarshal([]byte(s), &obj); err != nil {
		return nil, err
	}
	jsonData, _ := json.Marshal(obj)
	dec := json.NewDecoder(bytes.NewReader(jsonData))
	dec.DisallowUnknownFields()
	if err := dec.Decode(&scopeRules); err != nil {
		//logger.Debug("unmarshal to scope rules, try to unmarshal []PolicyRule", jsonData)
		var rules []rbacv1.PolicyRule
		if err := json.Unmarshal(jsonData, &rules); err == nil {
			scopeRules = make([]ScopePolicyRule, 0)
			scopeRules = append(scopeRules, ScopePolicyRule{
				Scope:      scopeType,
				ScopeNames: []string{ScopeAllName},
				Rules:      rules,
			})
			return scopeRules, nil
		} else {
			//fmt.Println("error:", err)
			return nil, err
		}
	}
	return scopeRules, nil
}
