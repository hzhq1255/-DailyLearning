#!/bin/bash
imageRepo=hzhq1255
collectImage=ansible
collectImageTag=2.10.8
echo "start build ansible collect image ...."
docker buildx build --push \
  --platform linux/amd64,linux/arm64  \
  -t ${imageRepo}/${collectImage}:${collectImageTag} \
  -f Dockerfile-collect .

controllerImage=ansible-controller
controllerImageTag=0.1
echo "start build ansible controller image ..."
docker buildx build --push \
   --platform linux/amd64,linux/arm64 \
   -t ${imageRepo}/${controllerImage}:${controllerImageTag} \
   -f Dockerfile-controller .
