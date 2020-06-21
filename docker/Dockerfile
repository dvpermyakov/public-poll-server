FROM openjdk:8-jre-alpine

RUN mkdir /app
WORKDIR /app
USER root

ENV JAR_NAME public-poll-server-0.0.4.jar
RUN apk --no-cache add curl
RUN curl -L "https://dl.bintray.com/dvpermyakov/maven/com/dvpermyakov/${JAR_NAME}" -o $JAR_NAME

CMD java -server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:InitialRAMFraction=2 -XX:MinRAMFraction=2 -XX:MaxRAMFraction=2 -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -jar $JAR_NAME