package com.ivanprokic.sportticketconsumer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TicketProducerClientConfig {

    @Value("${ticket-producer.url}")
    private String ticketProducerUrl;

    @Bean
    public TicketProducerClient ticketProducerClient() {
        RestClient restClient = RestClient.builder().baseUrl(ticketProducerUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        
        return factory.createClient(TicketProducerClient.class);
    }
}
