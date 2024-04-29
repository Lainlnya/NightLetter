FROM openjdk:17-alpine

ARG JAR_FILE=/back-end/build/libs/NightLetter-1.0.1.jar

COPY ${JAR_FILE} ./NightLetter-1.0.1.jar

ENTRYPOINT ["java","-jar", "NightLetter-1.0.1.jar"]