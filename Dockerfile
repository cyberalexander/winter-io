# syntax=docker/dockerfile:1
FROM openjdk:17-slim AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw clean -e -B package
COPY src ./src
ARG JAR_FILE=target/*jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java",\
            "-Djava.security.egd=file:/dev/./urandom",\
            "-jar",\
            "./app.jar"]

# commands:
# docker build -t winter-io .
# docker run winter-io:latest