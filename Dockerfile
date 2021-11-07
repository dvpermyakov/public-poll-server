FROM openjdk:11.0.7-jre

RUN mkdir -p /server_poll_fs

ARG VERSION
ENV JAR_NAME public-poll-server-${VERSION}.jar
COPY ./build/libs/$JAR_NAME /app/$JAR_NAME
WORKDIR /app

CMD java -server -jar $JAR_NAME