FROM sapmachine:26-jdk-ubuntu-noble
ARG JAR_FILE=build/libs/tilgungsplan_rechner-1.0.jar
COPY ${JAR_FILE} tilgungsplan_rechner.jar
ENTRYPOINT ["java","-jar","/tilgungsplan_rechner.jar"]
EXPOSE 8080