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

  mvn clean package -Pcontainer

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
