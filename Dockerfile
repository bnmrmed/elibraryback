FROM adoptopenjdk/maven-openjdk11
COPY . ./opt
WORKDIR /opt
RUN mvn clean install
EXPOSE 8080
RUN mv target/elibrary-back-*.jar target/elibrary.jar
ENTRYPOINT ["java","-jar","/opt/target/elibrary.jar"]