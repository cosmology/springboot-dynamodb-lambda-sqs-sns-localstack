package com.ivanprokic.dynamodblambdafunction.event;

public record TicketEvent(String action, Tickets ticket) {
    public record Tickets(String id, String eventType, String title, String publishedAt) {
    }
}
