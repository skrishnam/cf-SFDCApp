#!/bin/bash

if [ -n "$1" ]
then
  cf t
  cf cs p-config-server standard config-service
  cf cs p-service-registry standard eureka-service
  cf cs p-redis shared-vm data-grid-service
  #cf cs p-circuit-breaker-dashboard standard monitor-dashboard
  csJSONStr={\"tag\":\"sfdcgateway\",\"uri\":\"http://sfdcapigateway.$1\"}
  echo $csJSONStr
  cf cups sfdcgateway -p ${csJSONStr}
  echo -n "Add the git repo for config files in config-service before continuing!"
  echo -n "Also make sure eureka-service instance is UP before continuing!"
  read
  cf p -f ./manifest-all.yml
  curl http://sfdcapigateway.$1/authservice/oauth2
  curl http://sfdcapigateway.$1/accountservice/accounts
  curl http://sfdcapigateway.$1/accountservice/opp_by_accts
  curl http://sfdcapigateway.$1/contactservice/contact/003i000000eXDVVAA4
  curl http://sfdcapigateway.$1/opportunityservice/opportunity/006i000000HiNOyAAN
else
  echo "Usage: deploy <app domain name>"
fi
