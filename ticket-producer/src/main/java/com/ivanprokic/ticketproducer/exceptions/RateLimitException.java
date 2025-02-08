package com.ivanprokic.ticketproducer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends RuntimeException {

  public RateLimitException(final String message) {
    super(message);
  }

  public ApiErrorMessage toApiErrorMessage(final String path) {
    ApiErrorMessage errorMessage = new ApiErrorMessage();
    errorMessage.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    errorMessage.setError(HttpStatus.TOO_MANY_REQUESTS.name());
    errorMessage.setMessage(this.getMessage());
    errorMessage.setPath(path);
    return errorMessage;
  }
}
