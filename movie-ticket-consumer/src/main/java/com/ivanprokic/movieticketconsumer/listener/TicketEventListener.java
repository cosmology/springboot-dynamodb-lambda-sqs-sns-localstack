package com.ivanprokic.movieticketconsumer.listener;

import com.ivanprokic.movieticketconsumer.event.TicketEvent;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TicketEventListener {
    
    @Value("${aws.sqs.destination}")
    private String awsSqsDestination;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @SqsListener("${aws.sqs.destination}")
    public void sqsListener(TicketEvent ticketEvent) {
        log.info("TicketEventListener ****************** Received ticketEvent eventType: {}", ticketEvent.ticket().eventType());
        simpMessagingTemplate.convertAndSend("/topic/ticket", ticketEvent);
    }
}
