package com.ivanprokic.sportticketconsumer.client;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/api/ticket")
public interface TicketProducerClient {

    @GetExchange
    List<TicketResponse> getTicket();
}
