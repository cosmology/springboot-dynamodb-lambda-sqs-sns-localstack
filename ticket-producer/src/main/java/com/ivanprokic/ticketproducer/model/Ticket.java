package com.ivanprokic.ticketproducer.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@DynamoDbBean
public class Ticket {
  private String id;
  private String traceId;
  private String eventType;
  private String title;
  private Instant publishedAt;

  @DynamoDbPartitionKey
  public String getId() {
    return id;
  }

  public Ticket(String title, String eventType) {
    this.id = UUID.randomUUID().toString();
    this.eventType = eventType;
    this.title = title;
    this.publishedAt = Instant.now();
  }
}
