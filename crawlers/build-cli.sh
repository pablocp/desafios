#!/bin/bash

mvn -f reddit-crawler-proto/pom.xml package install
mvn -f rabbitmq-rpc-transport/pom.xml package install
mvn -f reddit-crawler-server/pom.xml package install
mvn -f reddit-crawler-cli/pom.xml package install
