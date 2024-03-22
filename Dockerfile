FROM openjdk:21-jdk
LABEL authors="ivkpo"
VOLUME /cherry-pick-bot
COPY build/libs/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]