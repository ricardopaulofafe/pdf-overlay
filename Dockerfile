FROM openjdk:8-jdk-alpine
RUN addgroup -S elaa && adduser -S elaa -G elaa
USER elaa:elaa
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} elaa-overlay.jar
ENTRYPOINT ["java","-jar","/elaa-overlay.jar"]