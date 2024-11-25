package com.ivanprokic.ticketproducer.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTicketRequest {

    @NotBlank
    @Schema(example = "Las Vegas Knights - LA Kings @ Nov 21st, Allegiant Arena")
    private String title;

    @NotBlank
    @Schema(example = "sport")
    private String eventType;
}
