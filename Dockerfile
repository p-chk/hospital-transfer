FROM gradle:7.4.1-jdk-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
USER root

FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=hospital-backend-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE /home/gradle/src/build/libs/${ARTIFACT_NAME} .
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}