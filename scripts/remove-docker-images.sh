#!/usr/bin/env bash

docker rmi com.ivanprokic/ticket-producer:1.0.0 -f
docker rmi com.ivanprokic/sport-ticket-consumer:1.0.0 -f
docker rmi com.ivanprokic/movie-ticket-consumer:1.0.0 -f
