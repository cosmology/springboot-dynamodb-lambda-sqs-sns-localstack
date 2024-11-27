package com.ivanprokic.movieticketconsumer.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long id) {
        super("Movie id '" + id + "' does not exist in our records");
    }
}
