FROM ubuntu:latest
LABEL authors="ivkpo"
VOLUME /cherry-pick-bot
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]