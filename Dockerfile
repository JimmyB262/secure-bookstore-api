# Build stage
FROM maven:3.9.11-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM quay.io/wildfly/wildfly:32.0.1.Final-jdk17

# Limit JVM memory
ENV JAVA_OPTS="-Xms128m -Xmx384m"

COPY --from=build /app/target/Helloworld-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

