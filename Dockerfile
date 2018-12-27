FROM openjdk:8-jdk-alpine

RUN apk add --no-cache bash

ADD build/libs/*.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-Dspring.profiles.active=production","-jar", "app.jar"]