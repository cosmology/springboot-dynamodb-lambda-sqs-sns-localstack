package com.ivanprokic.movieticketconsumer.client;

import java.util.Date;

public record TicketResponse(String id, String eventType, String title, Date publishedAt) {
}
