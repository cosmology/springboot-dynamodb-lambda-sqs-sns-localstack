package com.ivanprokic.movieticketconsumer.client;

import java.util.List;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/ticket")
public interface TicketProducerClient {

  @GetExchange
  List<TicketResponse> getTicket();
}
