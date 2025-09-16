# Use your WildFly version image
FROM quay.io/wildfly/wildfly:32.0.1.Final

# Copy your built WAR into the deployments folder
COPY target/Helloworld-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

# Expose the port WildFly listens on (default 8080)
EXPOSE 8080
