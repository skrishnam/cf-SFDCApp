#!/bin/bash

mvn clean
mvn package -DskipTests
pushd configServer
nohup mvn spring-boot:run&
popd
pushd eurekaServer
nohup mvn spring-boot:run&
popd
sleep 30
pushd authService
nohup mvn spring-boot:run&
popd
pushd accountService
nohup mvn spring-boot:run&
popd
pushd contactService
nohup mvn spring-boot:run&
popd
pushd opportunityService
nohup mvn spring-boot:run&
popd
pushd sfdcapigateway
nohup mvn spring-boot:run&
popd
pushd sfdcwebapp
nohup mvn spring-boot:run&
popd
pushd hystrixDashboard
nohup mvn spring-boot:run&
popd
sleep 100
curl http://localhost:9011/authservice/oauth2
curl http://localhost:9011/accountservice/accounts
curl http://localhost:9011/accountservice/opp_by_accts
curl http://localhost:9011/contactservice/contact/003i000000eXDVVAA4
curl http://localhost:9011/opportunityservice/opportunity/006i000000HiNOyAAN
