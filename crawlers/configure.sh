#!/bin/bash

function assert_installed() {
  COMMAND=$1
  URL=$2
  command -v $COMMAND >/dev/null 2>&1 || { echo "I need $COMMAND to execute. Please check $URL for installation instructions."; exit 1; }
}

assert_installed mvn http://maven.apache.org/install.html
assert_installed docker https://docs.docker.com/install/
assert_installed kubectl https://kubernetes.io/docs/tasks/tools/install-kubectl/

DOCKER_USER=$(docker info | sed '/Username:/!d;s/.* //')

if [ -z "$DOCKER_USER" ]
then
  docker login || exit 1
  DOCKER_USER=$(docker info | sed '/Username:/!d;s/.* //')
fi

read -p "Token do bot do Telegram: " TELEGRAM_BOT_TOKEN

sed -e "s/::DOCKER_USER::/$DOCKER_USER/" build.sh.template > build.sh
sed -e "s/::DOCKER_USER::/$DOCKER_USER/" deploy.sh.template > deploy.sh
sed -e "s/::DOCKER_USER::/$DOCKER_USER/"               \
    -e "s/::TELEGRAM_BOT_TOKEN::/$TELEGRAM_BOT_TOKEN/" \
    deployment.yaml.template > deployment.yaml
