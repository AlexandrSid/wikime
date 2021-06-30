#build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11.0.7-jdk-slim
WORKDIR /app
COPY --from=build /home/app/target/wikime-1.0-SNAPSHOT.jar /wikime.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/wikime.jar"]