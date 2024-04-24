FROM openjdk:17-alpine

ARG JAR_FILE=./build/libs/nightletter-1.0.1.jar

COPY ${JAR_FILE} ./nightletter-server.jar

ENTRYPOINT ["java","-jar", "nightletter-server.jar"]