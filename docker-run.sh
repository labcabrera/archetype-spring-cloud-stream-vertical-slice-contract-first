#!/bin/bash

./gradlew clean build -x test

docker stop sample-spring-cloud-stream-vertical-slice

docker rm sample-spring-cloud-stream-vertical-slice

docker rmi labcabrera/sample-spring-cloud-stream-vertical-slice:latest

docker build -t labcabrera/sample-spring-cloud-stream-vertical-slice:latest .

#TODO update config variables as needed
docker run -d --name sample-spring-cloud-stream-vertical-slice -p 8082:8082 \
  -e JAVA_OPTS="-Xms512m -Xmx1024m" \
  -e LOG_LEVEL="DEBUG" \
  labcabrera/sample-spring-cloud-stream-vertical-slice:latest

docker logs -f sample-spring-cloud-stream-vertical-slice
