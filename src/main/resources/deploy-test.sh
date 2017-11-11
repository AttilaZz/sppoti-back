#!/bin/bash

echo "Deploying sppoti application to the server ..."

if [ "$1" == "" ]
then
echo "Version is required"
exit 0
fi

mvn clean package deploy:deploy-file \
                     -Pcontainer \
                     -DskipTests \
                     -DPROJECT_VERSION=$1 \
                     -Durl=scpexe://ec2-user@ec2-54-194-211-162.eu-west-1.compute.amazonaws.com/home/ec2-user/ \
                     -DrepositoryId=ssh-repository \
                     -Dfile=SppotiRunApp/target/sppoti-app.war \
                     -DuniqueVersion=false \
                     -Dversion=$1 \
                     -Dpackaging=war \
                     -DpomFile=SppotiRunApp/pom.xml \
                     -DgroupId=sppoti \
                     -DartifactId=sppoti-application \
                     -DgeneratePom=false \
                     --settings src/main/resources/settings.xml