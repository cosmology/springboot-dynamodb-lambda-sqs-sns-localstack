package com.ivanprokic.ticketproducer.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
public class ApiErrorMessage {

  private UUID id = UUID.randomUUID();
  private int status = 200;
  private String error = "";
  private String message = "";
  private LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
  private String path = "";
}
