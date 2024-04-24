FROM openjdk:17-alpine

ARG JAR_FILE=./back-end/build/libs/nightletter-server.jar

COPY ${JAR_FILE} ./nightletter-server.jar

ENTRYPOINT ["java","-jar", "nightletter-1.0.1.jar"]