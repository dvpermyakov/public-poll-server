FROM openjdk:11.0.7-jre

ARG SQL_DATABASE_USER
ENV SQL_DATABASE_USER $SQL_DATABASE_USER
ARG SQL_DATABASE_PASSWORD
ENV SQL_DATABASE_PASSWORD $SQL_DATABASE_PASSWORD
ARG SQL_DATABASE_INSTANCE
ENV SQL_DATABASE_INSTANCE $SQL_DATABASE_INSTANCE

ARG KAFKA_DATABASE_USER
ENV KAFKA_DATABASE_USER $KAFKA_DATABASE_USER
ARG KAFKA_DATABASE_PASSWORD
ENV KAFKA_DATABASE_PASSWORD $KAFKA_DATABASE_PASSWORD
ARG KAFKA_DATABASE_INSTANCE
ENV KAFKA_DATABASE_INSTANCE $KAFKA_DATABASE_INSTANCE

ARG ELASTIC_AUTH_BASIC_TOKEN
ENV ELASTIC_AUTH_BASIC_TOKEN $ELASTIC_AUTH_BASIC_TOKEN
ARG ELASTIC_INSTANCE_URL
ENV ELASTIC_INSTANCE_URL $ELASTIC_INSTANCE_URL

ARG VERSION
ENV JAR_NAME public-poll-server-${VERSION}.jar
COPY ./build/libs/$JAR_NAME /app/$JAR_NAME
WORKDIR /app

CMD java -server -jar $JAR_NAME