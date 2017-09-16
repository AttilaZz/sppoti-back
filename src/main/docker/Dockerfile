FROM java:8

ENV SERVER_PORT 8080
EXPOSE $SERVER_PORT

VOLUME /tmp

ADD sppoti-application-test.war app.war

RUN bash -c 'touch /app.war'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]