FROM openjdk:11-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/elibrary-back-*.jar
ADD ${JAR_FILE} elibrary.jar
ENTRYPOINT ["java","-jar","/elibrary.jar"]