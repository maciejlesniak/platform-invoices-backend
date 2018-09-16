FROM anapsix/alpine-java:8_jdk
MAINTAINER docker@sparkidea.pl

VOLUME /tmp
ADD *.jar app.jar

EXPOSE 8080
ENTRYPOINT java -jar app.jar
