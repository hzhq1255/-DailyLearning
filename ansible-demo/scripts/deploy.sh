#!/bin/bash
# deploy ansible-collect
function apply() {
    echo "start apply"
    if [ "$(which helm)" != "" ]; then
        helm install ansible-collect ./ansible-collect
    else
      echo "not found helm"
    fi
}
