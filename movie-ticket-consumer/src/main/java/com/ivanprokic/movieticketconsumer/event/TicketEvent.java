package com.ivanprokic.movieticketconsumer.event;

import java.util.Date;

public record TicketEvent(String action, Ticket ticket) {
    public record Ticket(String id, String eventType, String title, Date publishedAt) {
    }
}
