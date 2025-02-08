package com.ivanprokic.ticketproducer.service;

import com.ivanprokic.ticketproducer.model.Ticket;
import java.util.List;
import org.springframework.web.service.annotation.GetExchange;

public interface TicketService {
  Ticket saveTicket(Ticket ticket);

  @GetExchange
  List<Ticket> getTickets();

  Ticket validateAndGetTicket(String id);

  void deleteTicket(String id);
}
