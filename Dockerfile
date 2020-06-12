FROM openjdk:8-jre-alpine

ENV APPLICATION_USER admin
RUN adduser -D -g '' $APPLICATION_USER
RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

# don't use local files! need to download it from maven repo
ARG VERSION
ENV JAR_NAME public-poll-server-${VERSION}.jar

COPY ./build/libs/$JAR_NAME /app/$JAR_NAME
WORKDIR /app

CMD java -server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:InitialRAMFraction=2 -XX:MinRAMFraction=2 -XX:MaxRAMFraction=2 -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -jar $JAR_NAME