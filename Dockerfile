# Use official WildFly image from Docker Hub
FROM jboss/wildfly:35.0.1.Final

# Set environment variables (optional)
ENV WILDFLY_HOME /opt/jboss/wildfly

# Copy your WAR file to the deployments folder
# Adjust the WAR file name if necessary
COPY target/Helloworld-1.0-SNAPSHOT.war $WILDFLY_HOME/standalone/deployments/

# Expose WildFly default ports
EXPOSE 8080 9990

# Run WildFly in standalone mode
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

