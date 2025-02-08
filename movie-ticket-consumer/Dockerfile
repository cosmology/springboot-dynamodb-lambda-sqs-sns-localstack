FROM maven:3.9.9-amazoncorretto-21-alpine

RUN apk add --no-cache bash curl jq

WORKDIR /app

COPY ./target/movie-ticket-consumer-1.0.0.jar entrypoint.sh /app/

RUN chmod +x /app/entrypoint.sh

EXPOSE 9082

ENTRYPOINT ["/app/entrypoint.sh"]

