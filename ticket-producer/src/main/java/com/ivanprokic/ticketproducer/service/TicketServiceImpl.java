package com.ivanprokic.ticketproducer.service;

import com.ivanprokic.ticketproducer.exception.TicketNotFoundException;
import com.ivanprokic.ticketproducer.model.Ticket;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final DynamoDbTemplate dynamoDbTemplate;

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return dynamoDbTemplate.save(ticket);
    }

    @Override
    public List<Ticket> getTickets() {
        PageIterable<Ticket> ticket = dynamoDbTemplate.scanAll(Ticket.class);
        return StreamSupport.stream(ticket.spliterator(), false)
                .flatMap(page -> page.items().stream())
                .sorted((n1, n2) -> n2.getPublishedAt().compareTo(n1.getPublishedAt()))
                .toList();
    }

    @Override
    public Ticket validateAndGetTicket(String id) {
        Key key = Key.builder().partitionValue(id).build();
        Ticket ticket = dynamoDbTemplate.load(key, Ticket.class);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with id %s not found".formatted(id));
        }
        return ticket;
    }

    @Override
    public void deleteTicket(String id) {
        Ticket ticket = validateAndGetTicket(id);
        dynamoDbTemplate.delete(ticket);
    }
}
