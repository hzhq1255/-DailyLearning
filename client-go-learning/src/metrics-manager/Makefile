VERSION ?= v1.0.0
BUILD_DIR ?= build

GOARCH ?= $(shell go env GOARCH)
GOOS ?= $(shell go env GOOS)
CGO_ENABLED ?= $(shell go env CGO_ENABLED)
GO_LDFLAGS ?= -s -w -extldflags \"-static\"

PKG_NAME = github.com/yakumioto/go-makefile-example
IMAGE_NAME = hzhq1255/metrics-controller
IMAGE_TAG = v1

override timestamp = $(shell date '+%s')
override app = $(filter-out $@,$(MAKECMDGOALS))
override package = $(PKG_NAME)/$(filter-out $@,$(MAKECMDGOALS))
override output_build_dir = bin
override output_test_dir = $(BUILD_DIR)/tests

%:
	@:

.PHONY : test build docker-build clean image-metrics

image-metrics:
	@echo "clean last build ..."
	rm -rf bin
	@echo "build test image"
	GOOS=linux GOARCH=amd64 go build -o  bin/metrics_controller app/metrics_list_watch.go
	@echo $(PWD)
	docker buildx build --push --platform linux/amd64 -t "$(IMAGE_NAME):$(IMAGE_TAG)" .
test:
	@echo "Testing $(package) ..."
	@mkdir -p $(output_test_dir)
	go test -coverprofile=$(output_test_dir)/$(timestamp).out $(package)

build:
	@echo "Building $(app) app..."
	@mkdir -p $(output_build_dir)
	CGO_ENABLED=$(CGO_ENABLED) GOOS=$(GOOS) GOARCH=$(GOARCH) go build -o $(output_build_dir)/$(app) -ldflags '$(GO_LDFLAGS)' $(PKG_NAME)/cmd/$(app)

docker-build:
	@echo "Building $(app) app in docker..."

	@echo "Building vendor..."
	@go mod vendor

	@echo "Building image..."
	@docker build \
           		--build-arg command="CGO_ENABLED=$(CGO_ENABLED) go build -o /app -ldflags '$(GO_LDFLAGS)' $(PKG_NAME)/cmd/$(app)" \
           		-t $(IMAGE_NAME)$(app):$(VERSION) -f images/$(app)/Dockerfile .

clean:
	@echo "Cleaning..."
	@rm -rf build/*
	@rm -rf vendor