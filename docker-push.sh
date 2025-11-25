#!/bin/bash

./gradlew clean build -x test

docker build -t labcabrera/sample-spring-cloud-stream-vertical-slice:latest .

docker tag labcabrera/sample-spring-cloud-stream-vertical-slice:latest labcabrera/sample-spring-cloud-stream-vertical-slice:latest

docker push labcabrera/sample-spring-cloud-stream-vertical-slice:latest
