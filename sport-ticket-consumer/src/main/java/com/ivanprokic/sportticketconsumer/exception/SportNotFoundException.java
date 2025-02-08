package com.ivanprokic.sportticketconsumer.exception;

public class SportNotFoundException extends RuntimeException {
  public SportNotFoundException(Long id) {
    super(String.format("Sport event id '%d' does not exist in our records", id));
  }
}
