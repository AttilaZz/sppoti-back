# SPPOTI API PROJECT

# INSTALLATION

## To install all dependencies and runs tests

mvn clean install

### To skip tests

mvn clean install -DskipTests

## To create the package without installing in the local repository

mvn clean package

### To skip tests

mvn clean package -DskipTests

# DEPLOIMENT

1. Move to project root
2. execute: sh src/main/resources/deploy.sh {version}

> The script will run mvn deploy with the needed parameters

# DOCKER

## CREATE the docker image

mvn clean package docker:build

## PUSH the image to the docker repository

mvn clean package docker:build docker:push

## RUN the image in the internal server of your machine

mvn clean package docker:build docker:run
