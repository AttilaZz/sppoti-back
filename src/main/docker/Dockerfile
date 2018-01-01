FROM java:8

EXPOSE 8080

VOLUME /tmp

ADD sppoti-app.war app.war

RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=test","-jar","/app.war"]
