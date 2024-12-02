#!/usr/bin/env bash

docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-filebeat:latest -f
docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-sport-consumer:latest -f
docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-movie-consumer:latest -f
docker rmi aws-localstack-spring-boot-dynamodb-lambda-sqs-sns-producer:latest -f
docker rmi ivanprokic/ticket-producer:1.0.0 -f
docker rmi ivanprokic/sport-ticket-consumer:1.0.0 -f
docker rmi ivanprokic/movie-ticket-consumer:1.0.0 -f
