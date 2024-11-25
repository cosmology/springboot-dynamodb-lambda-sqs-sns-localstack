package com.ivanprokic.movieticketconsumer.controller;

import com.ivanprokic.movieticketconsumer.client.TicketProducerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class TicketController {

    private final TicketProducerClient ticketProducerClient;

    @GetMapping(value = {"/", "/ticket"})
    public String getTicket(Model model) {
        model.addAttribute("ticketList", ticketProducerClient.getTicket());
        return "ticket";
    }
}
