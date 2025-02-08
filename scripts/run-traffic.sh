#!/bin/bash

# This script is used to generate traffic to the Thrre API servers.
# It creates, retrieves, searches, and deletes product items.

HOST=${HOST:-localhost}
PRODUCER_SERVICE_PORT=${PRODUCER_SERVICE_PORT:-9080}
SPORT_CONSUMER_PORT=${SPORT_CONSUMER_PORT:-9081}
MOVIE_CONSUMER_PORT=${MOVIE_CONSUMER_PORT:-9082}

BASE_URL="http://$HOST:$PORT/api"

API1="http://localhost:$SPORT_CONSUMER_PORT/api/v1/sport"
API2="http://localhost:$MOVIE_CONSUMER_PORT/api/v1/movie"
API3="http://localhost:$PRODUCER_SERVICE_PORT/api/ticket"


# Function to make POST API calls
make_post_api_call() {
  local endpoint=$1
  local payload=$2

  # Perform the POST API call and capture the response
  response=$(curl -s -X POST "$endpoint" \
      -H 'Content-Type: application/json' \
      -d "$payload")

  echo "$response"
}

# Function to make GET API calls
make_get_api_call() {
  local endpoint=$1
  curl -s -X GET "$endpoint"
}

# Function to make DELETE API calls
make_delete_api_call() {
  local endpoint=$1
  curl -s -X DELETE "$endpoint"
}

# Function to extract ID from JSON without jq
extract_id_from_response() {
  local response=$1
  echo "$response" | grep -o '"id":"[^"]*"' | sed 's/"id":"//;s/"//'
}

# Payloads
PAYLOAD1='{
  "title": "Las Vegas Knights - LA Kings @ Nov 21st, Allegiant Arena",
  "eventType": "sport"
}'

PAYLOAD2='{
  "title": "Hat Trick",
  "eventType": "movie"
}'

PAYLOAD3_MOVIE='{
  "title": "Hat Trick",
  "eventType": "movie"
}'

PAYLOAD3_SPORT='{
  "title": "Zvezda - Partizan @ Marakana Jan 22, 2025",
  "eventType": "sport"
}'

# Counter for alternating calls to API3 and DELETE logic
counter=0

# Infinite loop to hit APIs
while true; do
  # Randomly select an API (1, 2, or 3)
  api_choice=$((RANDOM % 3 + 1))


  # POST create a ticket
  case $api_choice in
    1)
      response=$(make_post_api_call "$API1" "$PAYLOAD1")
      ;;
    2)
      response=$(make_post_api_call "$API2" "$PAYLOAD2")
      ;;
    3)
      if (( counter % 2 == 0 )); then
        response=$(make_post_api_call "$API3" "$PAYLOAD3_MOVIE")
      else
        response=$(make_post_api_call "$API3" "$PAYLOAD3_SPORT")
      fi
      ;;
  esac

  # Extract the ID from the response if present
  id=$(extract_id_from_response "$response")

  if [[ -n "$id" ]]; then
    # GET created ticket
    make_get_api_call "$API1/$id"
    make_get_api_call "$API2/$id"
    make_get_api_call "$API3/$id"

    # GET all tickets
    make_get_api_call "$API1"
    make_get_api_call "$API2"
    make_get_api_call "$API3"

    # DELETE ticket
    if (( counter % 2 == 1 )); then
      make_delete_api_call "$API1/$id"
      make_delete_api_call "$API2/$id"
      make_delete_api_call "$API3/$id"
    fi
  fi

  # Increment the counter
  counter=$((counter + 1))

  # Sleep for a random duration between 1 and 3 seconds
  sleep $(awk -v min=1 -v max=3 'BEGIN{srand(); print min+rand()*(max-min)}')
done
