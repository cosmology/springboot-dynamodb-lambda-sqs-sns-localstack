#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanprokic"
APP_VERSION="1.0.0"
SKIP_TESTS="true"

# List of applications to build (name and folder are the same in this case)
APPS=("ticket-producer" "sport-ticket-consumer" "movie-ticket-consumer")

# Function to build a Docker image for a given project
build_image() {
  local app_name="$1"
  local docker_image_name="$DOCKER_IMAGE_PREFIX/$app_name:$APP_VERSION"
  echo "Building Docker image for $app_name..."
  ./mvnw clean spring-boot:build-image \
    --projects "$app_name" \
    -DskipTests="$SKIP_TESTS"
}

# Iterate over all applications and build their Docker images
for app in "${APPS[@]}"; do
  build_image "$app"
done

echo "All images built successfully."
