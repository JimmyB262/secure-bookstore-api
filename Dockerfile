# Use your WildFly version image
FROM quay.io/wildfly/wildfly:32.0.1.Final

# Copy your built WAR into the deployments folder
COPY target/yourapp.war /opt/jboss/wildfly/standalone/deployments/

# Expose the port WildFly listens on (default 8080)
EXPOSE 8080
