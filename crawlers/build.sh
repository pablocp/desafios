#!/bin/bash

echo "Compiling base files."
mvn -f reddit-crawler-proto/pom.xml package install
mvn -f rabbitmq-rpc-transport/pom.xml package install

echo "Compiling reddit crawler."
mvn -f reddit-crawler-server/pom.xml package install && \
docker build . -f reddit-crawler-server-easy.dockerfile -t nezkall/reddit-crawler-server:latest

echo "Compiling telegram bot."
mvn -f telegram-bot/pom.xml package install && \
docker build . -f telegram-bot-easy.dockerfile -t nezkall/telegram-bot:latest

#docker build . -f telegram-bot.dockerfile -t telegram-bot

echo "Done."