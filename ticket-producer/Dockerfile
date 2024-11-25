# Use an official OpenJDK image as the base image
FROM openjdk:21-slim

# Install only necessary tools (curl, jq) for health checks
RUN apt-get update && apt-get install -y --no-install-recommends curl jq && \
    rm -rf /var/lib/apt/lists/*

# Set a working directory
WORKDIR /app

# Copy JAR and entrypoint
COPY ./target/ticket-producer-1.0.0.jar entrypoint.sh /app/

# Expose the application port
EXPOSE 9080

# Use an entry point script for environment variable injection
RUN chmod +x /app/entrypoint.sh

# Use the entrypoint script to start the application
ENTRYPOINT ["/app/entrypoint.sh"]
