// Copyright 2021 CloudWeGo Authors 
// 
// Licensed under the Apache License, Version 2.0 (the "License"); 
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
// 
//     http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
// See the License for the specific language governing permissions and 
// limitations under the License. 
// 

namespace go hello

struct Request {
	1: string message (vt.max_size = "8", vt.prefix = "kitex-")
}

struct Response {
	1: string message
}

struct AddRequest {
    1: i64 first
    2: i64 second
}

struct AddResponse {
    1: i64 sum
}

// interface
service Hello {
    // func
    Response echo(1: Request req)
    AddResponse add(1: AddRequest req)
}

typedef string CalicoBGPMode
const CalicoBGPMode CalicoBGPFullMeshMode = "FullMesh"
const CalicoBGPMode CalicoRouteReflectorsMode = "RouteReflectors"

typedef string RRConfigType
const RRConfigType RRMasterConfig = "Master"
const RRConfigType RRExternalConfig = "External"

// CalicoBGPConfig Calico BGP 配置
struct CalicoBGPConfig {
    /* BGP 模式 FullMesh, RouteReflectors */
    1: required string CalicoBGPMode (go.tag = 'validate:"required,oneof=FullMesh RouteReflectors"'),
    /* ASN 配置 */
    2: required i64 ASN,
    /**
    *   RR 配置
    *   Master 指定Master节点作为RR
    *   External 指定外部RR
    **/
    3: optional string RRConfig ( go.tag = 'validate:"oneof=Master,External"'),
    /**
    *  RRConfig = Master
    *  为 RR 节点添加 Peer 的 IP:端口 字符串列表
    **/
    4: optional list<string> PeerAddresses;
    /**
    * RRConfig = External
    * 指定外部PR列表 IP:端口 字符串列表
    **/
    5: optional list<string> ExternalAddresses
}

struct NetworkPermission {
    /**计费策略，取值：
    PrePaid:包年包月
    PostPaid:按里计费
    默认值：PostPaid
    */
    1:optional string ChargeType = "PostPaid" (go.tag ='json:"ChargeType"validate:"oneof=PostPaid"default:"PostPaid"'),
    /**线路类型，取值：
    BGP:BGP
    默认值：BGP
    */
    2:optional string LanType = "BGP"(go.tag ='json:"LanType"validate:"oneof=BGP"default:"BGP'),
    /*带宽上限*/
    3:required i32 BandwidthLimit (go.tag = 'validate:"required,gte=1,lte=500"'),
}

struct clusterNetworkConfig {
	/*米VPC*/
	1:optional string VpcId,
	/*子网，逐步废弃，采用SubnetIds替代*/
	2:optional string SubnetId,
	/*容器网路类型，取值：[Calico F1 annel VPC-CNI Carma]*/
	3:required string Type (go.tag = 'validate:"required,oneof=Calico Flannel VPC-CNI Carma"'),
	/*容器网络模型，不同网铬类型有不同网铬模型，可选*/
	4:optional string Mode,
	/*米Pod CIDR*/
	5:optional string Podcidr,
	/*Pod Subnets */
	6:optional list<string>PodSubnets,
	/*米Service CIDR*/
	7:required string Servicecidr (go.tag = 'validate:"required"'),
	/*单节点Pod实例数量上限*/
	8:optional i32 MaxNodePodNumber,
	/*节点公网访问，取值：
	true:开启
	false:关闭
	*/
	9:required bool NodePublicAccess,
	/**API5 erver公网访问*/
	10:optional NetworkPermission ApiServerPublicAccess,
	/**apiserver的VIP,私有云可用*/
	11:optional string Vip,
	/*子网组，多子网以适配多AZ需求，和SubnetId需逻辑兼容*/
	12:optional list<string>SubnetIds,
	/*apiserver的public vip,私有云可用*/
	13:optional string Publicvip,
	/**
	* Calico BGP 模式配置文件
    **/
	14:optional CalicoBGPConfig CalicoBGPConfig,
}

struct NodeBaseConfig {
    /**已有节点，公有云场景*/
    1:optional ExistingNode ExistingNode,
    /**新增节点，公有云场景*/
    2:optional NewNode NewNode,
    /**自建节点，私有云场景*/
    3:optional OnPremiseNode OnPremiseNode,
}

struct ExistingNode {
    /**已有节点ID*/
    1:required string InstanceId (go.tag = 'validate:"required"'),
}

struct OnPremiseNode {
    /*自建节点的ID*/
    1:required string Id (go.tag = 'validate:"required"')
}

struct NewNode {
    /**可用区*/
    1:required string ZoneId (go.tag =  'validate:"required"'),
    /**子网*/
    2:required string SubnetId (go.tag =  'validate:"required"'),
    /**计费策略，取值：
    PrePaid:包年包月
    PostPaid:按里计费
    默认值：PostPaid
    */
    3:optional string ChargeType (go.tag ='json:"ChargeType"validate:"oneof=PostPaid"default:"PostPaid"'),
    /**计算规格ID*/
    4:required string InstanceTypeld,
    /**镜像，默认值：Velinux-VKE*/
    5:optional string ImageId = "Velinux-VKE",
    /**系统云盘*
    6:required Volume RootVolume (go.tag 'validate:"required"'),
    /**数据云盘*
    7:optional list<Volume>DataVolumes,
    /**节点数*/
    8:required i32 Amount (go.tag = 'validate:"required"'),
    /*openstack network id */
    9:required string NetworkId (go.tag = 'validate:"required"'),
    /**node子网cidr*/
    10:required string NodeSubnet (go.tag = 'validate:"required"'),
    /**计算规格1d*/
    11:required string ComputeProfileId (go.tag = 'validate:"required"')
}


