#!/bin/bash

# Check for required environment variables
: "${AWS_REGION:?AWS_REGION environment variable is required}"
: "${AWS_ACCESS_KEY_ID:?AWS_ACCESS_KEY_ID environment variable is required}"
: "${AWS_SECRET_ACCESS_KEY:?AWS_SECRET_ACCESS_KEY environment variable is required}"

# Start the application with the injected environment variables
exec java -jar movie-ticket-consumer-1.0.0.jar \
  -DAWS_REGION="$AWS_REGION" \
  -DAWS_ACCESS_KEY_ID="$AWS_ACCESS_KEY_ID" \
  -DAWS_SECRET_ACCESS_KEY="$AWS_SECRET_ACCESS_KEY"