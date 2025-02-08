package com.ivanprokic.dynamodblambdafunction.handler;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.ivanprokic.dynamodblambdafunction.event.TicketEvent;
import com.ivanprokic.dynamodblambdafunction.publisher.TicketEventPublisher;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DynamodbEventHandler implements Consumer<DynamodbEvent> {

  private final TicketEventPublisher ticketEventPublisher;

  private static final String ATTRIBUTE_ID = "id";
  private static final String ATTRIBUTE_EVENT_TYPE = "eventType";
  private static final String ATTRIBUTE_TRACE_ID = "traceId";
  private static final String ATTRIBUTE_TITLE = "title";
  private static final String ATTRIBUTE_PUBLISHED_AT = "publishedAt";

  @Override
  @Observed(
      name = "dynamodb-lambda-function:accept",
      contextualName = "dynamodb-lambda-function:accept",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public void accept(DynamodbEvent dynamodbEvent) {
    // dynamodb stream > lambda (this lambda can read the trace Id from the dynamodb field passed
    // Extract trace information from OpenTelemetry
    Span currentSpan = Span.current();
    String traceId = currentSpan.getSpanContext().getTraceId();
    String spanId = currentSpan.getSpanContext().getSpanId();

    log.info(
        "\n\nDynamodbEventHandler - Handling event with traceId: {} and spanId: {}",
        traceId,
        spanId);
    log.info("DynamodbEventHandler dynamodbEvent {} ", dynamodbEvent);

    dynamodbEvent.getRecords().stream()
        .map(ticketRecord -> toTicketEvent(ticketRecord, traceId, spanId))
        .forEach(ticketEventPublisher::publish);
  }

  private TicketEvent toTicketEvent(
      DynamodbEvent.DynamodbStreamRecord ticket, String traceId, String spanId) {

    Map<String, AttributeValue> image = ticket.getDynamodb().getNewImage();

    log.info("toTicketEvent ATTRIBUTE_EVENT_TYPE: {} ", image.get(ATTRIBUTE_EVENT_TYPE).getS());
    log.info("toTicketEvent ATTRIBUTE_TRACE_ID from DB: {} ", image.get(ATTRIBUTE_TRACE_ID).getS());
    log.info("toTicketEvent Injected traceId: {}, spanId: {}", traceId, spanId);

    return new TicketEvent(
        ticket.getEventName(),
        new TicketEvent.Tickets(
            image.get(ATTRIBUTE_ID).getS(),
            image.get(ATTRIBUTE_TRACE_ID).getS(), // traceId
            image.get(ATTRIBUTE_EVENT_TYPE).getS(),
            image.get(ATTRIBUTE_TITLE).getS(),
            image.get(ATTRIBUTE_PUBLISHED_AT).getS()));
  }
}
