FROM openjdk:21-jdk
LABEL authors="ivkpo"
VOLUME /cherry-pick-bot
COPY build/libs/*.jar app.jar
EXPOSE 8080

ARG GPG_KEY
ENV GPG_KEY=$GPG_KEY

ENTRYPOINT ["java", "-jar", "/app.jar"]