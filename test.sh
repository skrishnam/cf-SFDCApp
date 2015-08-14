#!/bin/bash

if [ -n "$1" ]
then
  csJSONStr={\"tag\":\"sfdcgateway\",\"uri\":\"http://sfdcapigateway.$1\"}
  echo $csJSONStr
  cf cups sfdcgateway -p ${csJSONStr}
else
  echo "Usage: deploy <app domain name>"
fi
