# SPPOTI API PROJECT

# INSTALLATION

## Environment variable to set before launching project in local ENV

<pre>

    MYSQL_USER = ""
    MYSQL_PASSWORD = ""
    MYSQL_PORT = ""
    spring.profiles.active = dev

</pre>

### PROFILES:
  
  1. DEV
  2. TEST
  3. PROD
  4. CONTAINER (for docker)
  
<pre>
  mvn clean <goal> -P<profile>
</pre>

## To install all dependencies and run tests

<pre>

  mvn clean install 

</pre>

### To run only tests

<pre>

  mvn clean test

</pre>

# DEPLOIMENT

## LEGACY

1. Move to project root
2. execute: 

<pre>

  sh src/main/resources/deploy.sh {version}

</pre>

> The script will run mvn deploy with the needed parameters

## DOCKER

### CREATE the docker image

<pre>

  mvn clean package -DskipTests -Pcontainer

</pre>

### CREATE the docker image and push to docker hub

> push is trigged automacally on install goal

<pre>

  mvn clean install -DskipTests -Pcontainer

</pre>

# PROFILES

> Both: maven profiles and spring profiles are used in the application

1. If no profile has been specified the default one is triggered (developpement)

2. To trigger production profile:

  <pre>
    Build: mvn clean install -Pproduction / container / test
  </pre>
  
  <pre>
    Run: java -jar <file> -Dspring.profiles.name=production / container / test
  </pre>
