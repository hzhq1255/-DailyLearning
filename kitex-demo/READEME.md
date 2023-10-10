# Kitex DEMO

## 关于 Kitex
Kitex 是一个 RPC 框架，既然是 RPC，底层就需要两大功能：

Serialization 序列化
Transport 传输
Kitex 框架及命令行工具，默认支持 thrift 和 proto3 两种 IDL，对应的 Kitex 支持 thrift 和 protobuf 两种序列化协议。 
传输上 Kitex 使用扩展的 thrift 作为底层的传输协议（注：thrift 既是 IDL 格式，同时也是序列化协议和传输协议）。
IDL 全称是 Interface Definition Language，接口定义语言。

为什么要使用 IDL

如果我们要进行 RPC，就需要知道对方的接口是什么，需要传什么参数，同时也需要知道返回值是什么样的，就好比两个人之间交流，需要保证在说的是同一个语言、同一件事。 
这时候，就需要通过 IDL 来约定双方的协议，就像在写代码的时候需要调用某个函数，我们需要知道函数签名一样。

Thrift IDL 语法可参考：[Thrift interface description language](https://thrift.apache.org/docs/idl)。

proto3 语法可参考：[Language Guide(proto3)](https://protobuf.dev/programming-guides/proto3/)。



> [getting-started](https://www.cloudwego.io/zh/docs/kitex/getting-started/)

## 项目结构

```text
.
├── build.sh # 构建脚本 sh build.sh
├── client # 客户端程序
│   └── main.go
├── conf # kitex 配置文件
│   └── kitex.yml
├── handler.go # 自动生成的 srv 文件
├── hello.thrift # thrift idl 文件
├── kitex_gen # kitex 代码生成的文件
│   └── api
│       ├── hello
│       │   ├── client.go
│       │   ├── hello.go
│       │   ├── invoker.go
│       │   └── server.go
│       ├── hello.go
│       ├── k-consts.go
│       └── k-hello.go
├── kitex_info.yaml # kitex 项目信息
├── main.go # 服务端程序
├── output # 构建输出文件夹
│   ├── bin
│   │   └── a.b.c
│   ├── bootstrap.sh
│   ├── conf
│   │   └── kitex.yml
│   └── settings.py
└── script # 脚本程序
    ├── bootstrap.sh
    └── settings.py
```


## 运行示例代码

`client` 每隔一秒轮询向 `server` 获取信息。

- 运行`server`

```shell
make run-server

cd hello && go run .
2023/10/10 15:52:19.552281 server.go:83: [Info] KITEX: server listen at addr=[::]:8888
```



- 运行`client`

```shell
make run-client

cd hello && go run ./client
2023/10/10 15:52:28 Response({Message:my request})
2023/10/10 15:52:29 Response({Message:my request})
```

## 添加方法

`hello.thrift`

```thrift

namespace go api

struct Request {
	1: string message
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
    Response echo(1: Request req)
    // add func AddResponse 新方法
    AddResponse add(1: AddRequest req)
}
```

## 生成代码

```shell
make generate

cd hello && kitex -module github.com/hzhq1255/daily-learning/kitex-demo -service 'a.b.c' hello.thrift
Adding apache/thrift@v0.13.0 to go.mod for generated code .......... Done

```

1. `hello/handler` 增加 `Add` 方法实现
2. `hello/kitex_gen` 为框架运行必要代码

## 实现服务渲染逻辑

```go
// Add implements the HelloImpl interface.
func (s *HelloImpl) Add(ctx context.Context, req *api.AddRequest) (resp *api.AddResponse, err error) {
	// TODO: Your code here...
	resp = &api.AddResponse{Sum: req.First + req.Second}
	return
}
```

## 增加客户端调用

`hello/client/main.go`

```go
for {
        req := &api.Request{Message: "my request"}
        resp, err := client.Echo(context.Background(), req)
        if err != nil {
                log.Fatal(err)
        }
        log.Println(resp)
        time.Sleep(time.Second)
        addReq := &api.AddRequest{First: 512, Second: 512}
        addResp, err := client.Add(context.Background(), addReq)
        if err != nil {
                log.Fatal(err)
        }
        log.Println(addResp)
        time.Sleep(time.Second)
}
```
## 重新运行

1. 运行 `server`

```shell
make run-server

cd hello && go run .
2023/10/10 16:51:10.316655 server.go:83: [Info] KITEX: server listen at addr=[::]:8888
```

2. 运行 `client`

```shell
make run-client                                                   daily-learning/git/master !
cd hello && go run ./client
2023/10/10 16:51:15 Response({Message:my request})
2023/10/10 16:51:16 AddResponse({Sum:15})
2023/10/10 16:51:17 Response({Message:my request})
2023/10/10 16:51:18 AddResponse({Sum:15})
2023/10/10 16:51:19 Response({Message:my request})

```


