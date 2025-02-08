package com.ivanprokic.dynamodblambdafunction.publisher;

import com.ivanprokic.dynamodblambdafunction.event.TicketEvent;
import com.ivanprokic.dynamodblambdafunction.properties.AwsProperties;
import io.awspring.cloud.sns.core.SnsTemplate;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TicketEventPublisher {

  private final SnsTemplate snsTemplate;
  private final AwsProperties awsProperties;

  private static final String TRACE_ID_KEY = "traceId";
  private static final String SPAN_ID_KEY = "spanId";

  private static final Logger logger = LoggerFactory.getLogger(TicketEventPublisher.class);

  @Observed(
      name = "dynamodb-lambda-function:publish",
      contextualName = "dynamodb-lambda-function:publish",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public void publish(TicketEvent ticketEvent) {

    log.info("\n\nIVAN TicketEventPublisher ticketEvent => {}" + ticketEvent);
    String topic = awsProperties.getSns().getDestination();

    // Extract trace information from OpenTelemetry
    Span currentSpan = Span.current();
    String traceId = currentSpan.getSpanContext().getTraceId();
    String spanId = currentSpan.getSpanContext().getSpanId();

    // Log Trace ID and add it to Ticket
    log.info("DYNAMODB EVENT PUBLISHER traceId {}, spanId {} ", traceId, spanId);
    log.info("DYNAMODB EVENT PUBLISHER currentSpan ", currentSpan);
    log.info("DYNAMODB EVENT PUBLISHER ticketEvent {}", ticketEvent);

    // logger.info("TicketEventPublisher messageAttributes => {}" + messageAttributes);
    // snsTemplate.convertAndSend(topic, ticketEvent);
    snsTemplate.convertAndSend(
        topic, ticketEvent, Map.of("event_type", ticketEvent.ticket().eventType()));
  }
}
