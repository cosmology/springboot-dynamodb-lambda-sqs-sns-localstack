package com.ivanprokic.dynamodblambdafunction.handler;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.ivanprokic.dynamodblambdafunction.event.TicketEvent;
import com.ivanprokic.dynamodblambdafunction.publisher.TicketEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DynamodbEventHandler implements Consumer<DynamodbEvent> {

    private final TicketEventPublisher ticketEventPublisher;

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_EVENT_TYPE = "eventType";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_PUBLISHED_AT = "publishedAt";

    @Override
    public void accept(DynamodbEvent dynamodbEvent) {
        log.info("======================= DynamodbEventHandler dynamodbEvent " + dynamodbEvent);
        dynamodbEvent.getRecords()
            .stream()
            .map(this::toTicketEvent)
            .forEach(ticketEventPublisher::publish);
    }

    private TicketEvent toTicketEvent(DynamodbEvent.DynamodbStreamRecord ticket) {

        log.info("======================= DynamodbEventHandler ticket " + ticket);
        Map<String, AttributeValue> image = ticket.getDynamodb().getNewImage();

        return new TicketEvent(
            ticket.getEventName(),
            new TicketEvent.Tickets(
                image.get(ATTRIBUTE_ID).getS(),
                image.get(ATTRIBUTE_EVENT_TYPE).getS(),
                image.get(ATTRIBUTE_TITLE).getS(),
                image.get(ATTRIBUTE_PUBLISHED_AT).getS()
            )
        );
    }
    
}
