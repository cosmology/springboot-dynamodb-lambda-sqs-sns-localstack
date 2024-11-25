package com.ivanprokic.ticketproducer.service;

import com.ivanprokic.ticketproducer.model.Ticket;

import java.util.List;

public interface TicketService {

    Ticket saveTicket(Ticket ticket);

    List<Ticket> getTickets();

    Ticket validateAndGetTicket(String id);

    void deleteTicket(String id);
}
