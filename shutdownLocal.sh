#!/bin/bash

kill -9 `ps -ef | grep spring-boot:run | grep -v grep | awk '{print $2}'`
