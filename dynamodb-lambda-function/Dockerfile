# Build stage using Maven
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration files
COPY ./pom.xml ./pom.xml

# Copy source code
COPY ./src ./src

# Disable Maven Daemon for reproducible builds
ENV MAVEN_OPTS "-Dmaven.repo.local=/root/.m2/repository"

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage using Amazon Corretto
FROM amazoncorretto:21-alpine
WORKDIR /app

# Add AWS OpenTelemetry agent
# ADD https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.21.1/aws-opentelemetry-agent.jar /app/aws-opentelemetry-agent.jar
ADD https://github.com/aws-observability/aws-otel-java-instrumentation/releases/download/v1.32.5/aws-opentelemetry-agent.jar /app/aws-opentelemetry-agent.jar
ENV JAVA_TOOL_OPTIONS "-javaagent:/app/aws-opentelemetry-agent.jar"

# Copy the built JAR from the build stage
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} ./app.jar

# OpenTelemetry agent configuration
ENV OTEL_TRACES_SAMPLER "always_on"
ENV OTEL_PROPAGATORS "tracecontext,baggage,xray"
ENV OTEL_RESOURCE_ATTRIBUTES "service.name=ticket-producer"
ENV OTEL_IMR_EXPORT_INTERVAL "10000"
ENV OTEL_EXPORTER_OTLP_ENDPOINT "http://localhost:4317"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
