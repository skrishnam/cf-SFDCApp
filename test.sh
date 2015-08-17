#!/bin/bash

if [ -n "$1" ]
then
  curl http://sfdcapigateway.$1/authservice/oauth2
  curl http://sfdcapigateway.$1/accountservice/accounts
  curl http://sfdcapigateway.$1/accountservice/opp_by_accts
  curl http://sfdcapigateway.$1/contactservice/contact/003i000000eXDVVAA4
  curl http://sfdcapigateway.$1/opportunityservice/opportunity/006i000000HiNOyAAN
else
  echo "Usage: test <app domain name>"
fi
