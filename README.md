# Running the Tilgunsplan Rechner with Docker:

1. Build again the project with the gradle Wrapper

   From within the directory project run:
   ```shell
    ./gradlew build
   ```
2. From within the project directory build the docker image:
   ```shell
    docker build -t tilgungsplan_rechner .
   ```
3. Once the docker image has been created, start the container:
   ```shell
    docker run -p 8080:8080 tilgungsplan_rechner
   ```
4. The Tilgungsplan Rechner Application will be reachable at
   http://localhost:8080/

# Running the Tilgungsplan Rechner Application as jar file:

1. From within the directory project run:
   ```shell
    ./gradlew build
   ```
2. Once the gradle build is done, run:
   ```shell
    java -jar build/libs/tilgungsplan_rechner-1.0.jar
   ```
3. The Tilgungsplan Rechner Application will be reachable at:
   http://localhost:8080/

# TODO: add more here
# Application Usage
Formule per il calcolo delle rate:
https://www.agos.it/piano-ammortamento-alla-francese/faq.aspx**