# Hertz Demo

> [getting-started](https://www.cloudwego.io/zh/docs/hertz/getting-started/)

## 快速开始

1. 初始化项目 `go mod init hertz-demo`
2. 下载依赖 `go get -u github.com/cloudwego/hertz@latest`
3. 新建 `main.go`

```go
package main 

import (
	"context"
	"github.com/cloudwego/hertz/pkg/app"
	"github.com/cloudwego/hertz/pkg/app/server"
	"github.com/cloudwego/hertz/pkg/common/utils"
	"github.com/cloudwego/hertz/pkg/protocol/consts"
)

func main() {
	h := server.Default()
	h.GET("/ping", func(c context.Context, ctx *app.RequestContext) {
		ctx.JSON(consts.StatusOK, utils.H{"message": "hertz"})
	})
	h.Spin()
}
```

4. 更新依赖 `go mod tidy`
5. 运行示例代码 `go run main.go`

```shell
go run main.go                                                                                                                                                                                        daily-learning/git/master !+
2023/10/10 20:16:20.211703 engine.go:666: [Debug] HERTZ: Method=GET    absolutePath=/ping                     --> handlerName=main.main.func1 (num=2 handlers)
2023/10/10 20:16:20.212118 engine.go:394: [Info] HERTZ: Using network library=netpoll
2023/10/10 20:16:20.212697 transport.go:115: [Info] HERTZ: HTTP server listening on address=[::]:8888
```

测试服务

```shell
curl http://localhost:8888/ping
{"message":"hertz"}
```

## 代码生成

1. 定义 `hello.thrift` 文件

```thrift
namespace go hello.world

service HelloService {
    string Hello(1: string name)
}
```

2. 执行命令生成代码文件

```shell
hz new --module github.com/hzhq1255/daily-learning/hertz-demo -idl hello.thrift
```

3. 整理依赖

```shell
go mod tidy 
```

4. 目录结构

```text
.
├── Makefile
├── README.md
├── biz
│   ├── handler
│   │   ├── hello
│   │   │   └── world
│   │   │       └── hello_service.go
│   │   └── ping.go
│   ├── model
│   │   └── hello
│   │       └── world
│   │           └── hello.go
│   └── router
│       ├── hello
│       │   └── world
│       │       ├── hello.go
│       │       └── middleware.go
│       └── register.go
├── build.sh
├── go.mod
├── go.sum
├── hello.thrift
├── main.go
├── router.go
├── router_gen.go
└── script
    └── bootstrap.sh

12 directories, 16 files


```

## 运行代码

编译启动 `server`

```shell
go build -o bin/hertz-demo && bin/hertz-demo

2023/10/10 20:57:38.568369 engine.go:666: [Debug] HERTZ: Method=GET    absolutePath=/ping                     --> handlerName=github.com/hzhq1255/daily-learning/hertz-demo/biz/handler.Ping (num=2 handlers)
2023/10/10 20:57:38.568499 engine.go:394: [Info] HERTZ: Using network library=netpoll
2023/10/10 20:57:38.568682 transport.go:115: [Info] HERTZ: HTTP server listening on address=[::]:8888


curl http://localhost:8888/ping
{"message":"hertz"}

```


