#!/bin/bash

echo "Pushing docker images"
docker push nezkall/reddit-crawler:latest
docker push nezkall/telegram-bot:latest

kubectl create -f kubernetes/deployment.yaml
