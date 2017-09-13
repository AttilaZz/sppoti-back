# SPPOTI API PROJECT

# INSTALLATION

## To install all dependencies and runs tests

<pre>

  mvn clean install

</pre>

### To skip tests

<pre>

  mvn clean install -DskipTests

</pre>

## To create the package without installing in the local repository

<pre>

  mvn clean package

</pre>

### To skip tests

<pre>

  mvn clean package -DskipTests

</pre>

# DEPLOIMENT

1. Move to project root
2. execute: 

<pre>

  sh src/main/resources/deploy.sh {version}

</pre>

> The script will run mvn deploy with the needed parameters

# DOCKER

## CREATE the docker image

<pre>

  mvn clean package docker:build

</pre>

## PUSH the image to the docker repository

<pre>

  mvn clean package docker:build docker:push

</pre>

## RUN the image in the internal server of your machine

<pre>

  mvn clean package docker:build docker:run

</pre>
