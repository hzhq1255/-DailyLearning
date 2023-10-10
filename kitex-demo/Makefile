

kitexVersion ?= latest
thriftgoVersion ?= latest

.PHONY: install
install: install-kitex install-thriftgo

.PHONY: install-kitex
install-kitex:
	go install github.com/cloudwego/kitex/tool/cmd/kitex@$(kitexVersion)
	kitex --version

.PHONY: install-thriftgo
install-thriftgo:
	go install github.com/cloudwego/thriftgo@$(thriftgoVersion)
	thriftgo --version
