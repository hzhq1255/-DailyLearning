project_name = github.com/hzhq1255/daily-learning/hertz-demo
hz_version ?= latest



.PHONY: download
download:
	@ls $(shell go env GOPATH)/bin/hz > /dev/null || go install github.com/cloudwego/hertz/cmd/hz@$(hz_version)

file_name ?= hello.thrift
.PHONY: hz-new
hz-new: download
	hz new -mod $(project_name) -idl $(file_name)

.PHONY: hz-update
hz-update:
	hz update -idl hello.thrift


.PHONY: build
build:
	go build -o bin/hertz-demo && bin/hertz-demo


.PHONY: run
run: build
	bin/hertz-demo