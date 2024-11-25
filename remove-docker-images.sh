#!/usr/bin/env bash

docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-consumer:latest
docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-producer:latest
docker rmi ivanprokic/ticket-producer:1.0.0
docker rmi ivanprokic/sport-ticket-consumer:1.0.0
docker rmi ivanprokic/movie-ticket-consumer:1.0.0
