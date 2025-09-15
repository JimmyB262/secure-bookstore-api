# Use official WildFly image
FROM quay.io/wildfly/wildfly:35.0.1.Final

# Copy WAR to deployments folder
COPY target/Helloworld-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

# Expose HTTP port
EXPOSE 8080

# Start WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
