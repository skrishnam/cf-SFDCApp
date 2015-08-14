#!/bin/bash

cf t
cf d -r authService
cf d -r accountService
cf d -r contactService
cf d -r opportunityService
cf d -r sfdcapigateway
cf d -r sfdcnodewebapp
cf d -r sfdcbootwebapp
cf ds sfdcgateway -f
cf ds data-grid-service -f
cf ds monitor-dashboard -f
cf ds eureka-service -f
cf ds config-service -f
cf a
cf s
