# Use an official Maven image with Temurin JDK as the base image
FROM maven:3.9.4-eclipse-temurin-21-alpine

# Install required dependencies in one step for efficiency
RUN apk add --no-cache bash curl jq

# Set a working directory
WORKDIR /app

# Combine COPY statements to minimize layers
COPY ./target/sport-ticket-consumer-1.0.0.jar entrypoint.sh /app/

# Make the entrypoint script executable
RUN chmod +x /app/entrypoint.sh

EXPOSE 9081

# Use the entrypoint script to start the application
ENTRYPOINT ["/app/entrypoint.sh"]

