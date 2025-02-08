FROM maven:3.9.9-amazoncorretto-21-alpine

RUN apk add --no-cache bash curl jq

WORKDIR /app

COPY ./target/sport-ticket-consumer-1.0.0.jar entrypoint.sh /app/

RUN chmod +x /app/entrypoint.sh

EXPOSE 9081

ENTRYPOINT ["/app/entrypoint.sh"]

