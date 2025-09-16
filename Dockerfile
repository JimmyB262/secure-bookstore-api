FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM quay.io/wildfly/wildfly:32.0.1.Final-jdk17

COPY --from=build /app/target/Helloworld-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080
