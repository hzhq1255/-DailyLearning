# kitex demo
moduleName ?= github.com/hzhq1255/daily-learning/kitex-demo


kitexVersion ?= latest
thriftgoVersion ?= latest
validatorVersion ?= latest


.PHONY: install
install: install-kitex install-thriftgo install-validator

.PHONY: install-kitex
install-kitex:
	@ls $(GOPATH)/bin/kitex > /dev/null || go install github.com/cloudwego/kitex/tool/cmd/kitex@$(kitexVersion)

.PHONY: install-thriftgo
install-thriftgo:
	@ls $(GOPATH)/bin/thriftgo > /dev/null || go install github.com/cloudwego/thriftgo@$(thriftgoVersion)

.PHONY: install-validator
install-validator:
	@ls $(GOPATH)/bin/thrift-gen-validator > /dev/null || go install github.com/cloudwego/thrift-gen-validator@$(validatorVersion)

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
	cd hello && kitex --thrift-plugin validator -module $(moduleName) -service kitex-demo hello.thrift
