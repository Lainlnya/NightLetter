FROM openjdk:17-alpine

ARG JAR_FILE=./build/libs/NightLetter.jar

COPY ${JAR_FILE} ./nightletter-server.jar

ENTRYPOINT ["java","-jar", "nightletter-server.jar"]