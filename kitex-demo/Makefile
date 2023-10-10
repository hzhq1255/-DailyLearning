# kitex demo
moduleName ?= github.com/hzhq1255/daily-learning/kitex-demo


kitexVersion ?= latest
thriftgoVersion ?= latest

.PHONY: install
install: install-kitex install-thriftgo

.PHONY: install-kitex
install-kitex:
	@ls $(GOPATH)/bin/kitex > /dev/null || go install github.com/cloudwego/kitex/tool/cmd/kitex@$(kitexVersion)
	@kitex --version

.PHONY: install-thriftgo
install-thriftgo:
	@ls $(GOPATH)/bin/thriftgo > /dev/null || go install github.com/cloudwego/thriftgo@$(thriftgoVersion)
	@thriftgo --version

### Build
.PHONY: build
build: install
	cd hello && sh build.sh

### RUN
.PHONY: run-server
run-server:
	cd hello && go run .

.PHONY: run-client
run-client:
	cd hello && go run ./client


### code generate
.PHONY: generate
generate: install
	cd hello && kitex -module $(moduleName) -service kitex-demo hello.thrift
