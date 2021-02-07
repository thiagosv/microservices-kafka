FROM openjdk:8-jre-alpine3.9

RUN apk add --no-cache openssl

#RUN adduser --disabled-password --home "/home/leitorftp" "leitorftp"
#USER leitorftp

COPY target/leitor-ftp-*.jar /home/root/leitor-ftp.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT [ "java", "-jar", "-Dspring-boot.run.profiles=docker", "/home/root/leitor-ftp.jar" ]