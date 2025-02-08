package com.ivanprokic.ticketproducer.service;

import com.ivanprokic.ticketproducer.exceptions.TicketNotFoundException;
import com.ivanprokic.ticketproducer.model.Ticket;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

  private final DynamoDbTemplate dynamoDbTemplate;

  @Override
  @Observed(
      name = "ticket-produce:saveTicket",
      contextualName = "ticket-produce:saveTicket",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public Ticket saveTicket(Ticket ticket) {
    // Lambda > dynamodb (add traceId as a field) >
    // Extract trace information from OpenTelemetry
    // Get the current span context
    SpanContext spanContext = Span.current().getSpanContext();

    // Check if the span is valid
    if (spanContext.isValid()) {
      // Get the trace ID
      String traceId = spanContext.getTraceId();
      String spanId = spanContext.getSpanId();

      // Log Trace ID and add it to Ticket
      log.info("TicketServiceImpl saveTicket with traceId: {}, spanId: {}", traceId, spanId);
      ticket.setTraceId(traceId);
    }

    return dynamoDbTemplate.save(ticket);
  }

  @Override
  @Observed(
      name = "ticket-produce:getTickets",
      contextualName = "ticket-produce:getTickets",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public List<Ticket> getTickets() {
    PageIterable<Ticket> ticket = dynamoDbTemplate.scanAll(Ticket.class);
    return StreamSupport.stream(ticket.spliterator(), false)
        .flatMap(page -> page.items().stream())
        .sorted((n1, n2) -> n2.getPublishedAt().compareTo(n1.getPublishedAt()))
        .toList();
  }

  @Override
  @Observed(
      name = "ticket-produce:validateAndGetTicket",
      contextualName = "ticket-produce:validateAndGetTicket")
  public Ticket validateAndGetTicket(String id) {
    Key key = Key.builder().partitionValue(id).build();
    log.info("Loading ticket from dynamodb with id {}", id);
    Ticket ticket = dynamoDbTemplate.load(key, Ticket.class);
    if (ticket == null) {
      throw new TicketNotFoundException("Ticket with id %s not found".formatted(id));
    }
    return ticket;
  }

  @Override
  @Observed(name = "ticket-produce:deleteTicket", contextualName = "ticket-produce:deleteTicket")
  public void deleteTicket(String id) {
    Ticket ticket = validateAndGetTicket(id);
    dynamoDbTemplate.delete(ticket);
  }
}
