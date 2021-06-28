FROM openjdk:11.0.7-jdk-slim
WORKDIR /app
COPY ./target/wikime-1.0-SNAPSHOT.jar /wikime.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/wikime.jar"]