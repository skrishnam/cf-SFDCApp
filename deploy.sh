#!/bin/bash

if [ -n "$1" ]
then
  cf t
  cf cs p-config-server standard config-service
  cf cs p-service-registry standard eureka-service
  cf cs p-redis shared-vm data-grid-service
  cf cs p-circuit-breaker-dashboard standard monitor-dashboard
  csJSONStr={\"tag\":\"sfdcgateway\",\"uri\":\"http://sfdcapigateway.$1\"}
  echo $csJSONStr
  cf cups sfdcgateway -p ${csJSONStr}
  echo -n "Add the git repo for config files in config-service before continuing!"
  read
  cf p -f ./manifest-all.yml
else
  echo "Usage: deploy <app domain name>"
fi
