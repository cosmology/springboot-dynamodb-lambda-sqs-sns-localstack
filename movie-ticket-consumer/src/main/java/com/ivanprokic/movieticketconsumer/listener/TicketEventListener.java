package com.ivanprokic.movieticketconsumer.listener;

import com.ivanprokic.movieticketconsumer.entity.Movie;
import com.ivanprokic.movieticketconsumer.event.TicketEvent;
import com.ivanprokic.movieticketconsumer.repository.MovieRepository;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TicketEventListener {

    private final MovieRepository movieRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    @Value("${aws.sqs.destination}")
    private String awsSqsDestination;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @SqsListener("${aws.sqs.destination}")
    public void sqsListener(TicketEvent ticketEvent) {
        log.info("10. TicketEventListener ****************** Received rawPayload: {}", ticketEvent);

        // Send the raw payload to the WebSocket topic
        simpMessagingTemplate.convertAndSend("/topic/ticket", ticketEvent);

        // Extract and set values for the Movie entity
        Movie movie = new Movie();
        movie.setEventType(ticketEvent.ticket().eventType());
        movie.setTitle(ticketEvent.ticket().title());

        // Convert the publishedAt string to OffsetDateTime
        String publishedAtStr = ticketEvent.ticket().publishedAt().toString();
        ZonedDateTime tmpTimestamp = ZonedDateTime.parse(publishedAtStr, formatter);
        movie.setPublishedAt(tmpTimestamp.toOffsetDateTime());

        // Save to the repository
        movieRepository.save(movie);

        log.info("TicketEventListener ****************** Saved movie entity: {}", movie);
    }
}
