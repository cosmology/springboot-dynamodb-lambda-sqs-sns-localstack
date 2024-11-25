package com.ivanprokic.ticketproducer.rest;

import com.ivanprokic.ticketproducer.model.Ticket;
import com.ivanprokic.ticketproducer.rest.dto.CreateTicketRequest;
import com.ivanprokic.ticketproducer.service.TicketService;
import com.ivanprokic.ticketproducer.service.RandomTicketGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final RandomTicketGenerator randomTicketGenerator;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Ticket createTicket(@Valid @RequestBody CreateTicketRequest request) {
        log.info("Create ticket {}", request);
        return ticketService.saveTicket(new Ticket(request.getTitle(), request.getEventType()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/randomly")
    public Ticket createTicketRandomly() {
        log.info("Create ticket randomly");
        return ticketService.saveTicket(randomTicketGenerator.getRandomly());
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable String id) {
        log.info("Get ticket with id {}", id);
        return ticketService.validateAndGetTicket(id);
    }

    @GetMapping
    public List<Ticket> getTicket() {
        return ticketService.getTickets();
    }

    @DeleteMapping("/{id}")
    public Ticket deleteTicket(@PathVariable String id) {
        log.info("Delete ticket with id {}", id);
        Ticket ticket = ticketService.validateAndGetTicket(id);
        ticketService.deleteTicket(id);
        return ticket;
    }
}
