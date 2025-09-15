# Use a valid WildFly image from Docker Hub
FROM jboss/wildfly:35.0.0.Final

# Set environment variable (optional)
ENV WILDFLY_HOME /opt/jboss/wildfly

# Copy your WAR file into WildFly deployments folder
# Make sure your WAR is built with `mvn clean package`
COPY target/Helloworld-1.0-SNAPSHOT.war $WILDFLY_HOME/standalone/deployments/

# Expose WildFly default HTTP and management ports
EXPOSE 8080 9990

# Start WildFly in standalone mode
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]


