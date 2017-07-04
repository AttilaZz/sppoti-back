#!/bin/bash

echo "Deploying sppoti application to the server ..."

if [ "$1" == "" ]
then
echo "Version is required"
exit 0
fi

mvn clean package deploy:deploy-file \
                     -Ptest \
                     -DskipTests \
                     -Durl=scpexe://ubuntu@ec2-34-253-168-21.eu-west-1.compute.amazonaws.com/home/ubuntu/ \
                     -DrepositoryId=ssh-repository \
                     -Dfile=SppotiRunApp/target/sppoti-application-test.war \
                     -DuniqueVersion=false \
                     -Dversion=$1 \
                     -Dpackaging=jar \
                     -DpomFile=SppotiRunApp/pom.xml \
                     -DgroupId=sppoti \
                     -DartifactId=sppoti-application \
                     -DgeneratePom=false \
                     --settings src/main/resources/settings.xml