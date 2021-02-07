FROM openjdk:8-jre-alpine3.9

RUN apk add --no-cache openssl

RUN apk add --no-cache openssl

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY target/leitor-ftp-*.jar /home/root/leitor-ftp.jar
ENV SPRING_PROFILES_ACTIVE=docker

CMD dockerize -wait tcp://postgresql:5432 \
    -wait tcp://ftpd_server:21 \
    -wait tcp://s3ninja:9000 \
    -wait tcp://kafka:9092 \
    --timeout 20s \
    java -jar /home/root/leitor-ftp.jar