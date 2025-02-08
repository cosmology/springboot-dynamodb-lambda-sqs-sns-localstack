package com.ivanprokic.movieticketconsumer.exception;

public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException(Long id) {
    super(String.format("Movie event id '%d' does not exist in our records", id));
  }
}
