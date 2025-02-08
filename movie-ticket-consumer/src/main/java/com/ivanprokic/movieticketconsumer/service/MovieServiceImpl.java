package com.ivanprokic.movieticketconsumer.service;

import com.ivanprokic.movieticketconsumer.entity.Movie;
import com.ivanprokic.movieticketconsumer.exception.MovieNotFoundException;
import com.ivanprokic.movieticketconsumer.repository.MovieRepository;
import io.micrometer.observation.annotation.Observed;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

  MovieRepository movieRepository;

  @Override
  @Observed(
      name = "post:createMovie",
      contextualName = "post:createMovie",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public Movie createMovie(Movie movie) {

    // Get the current span context
    SpanContext spanContext = Span.current().getSpanContext();

    // Check if the span is valid
    if (spanContext.isValid()) {
      // Get the trace ID
      String traceId = spanContext.getTraceId();
      String spanId = spanContext.getSpanId();

      // Log Trace ID and add it to Ticket
      log.info("MovieServiceImpl createMovie with traceId: {}, spanId: {}", traceId, spanId);
      movie.setTraceId(traceId);
    }

    return movieRepository.save(movie);
  }

  @Override
  public Movie getMovieById(Long id) {
    Optional<Movie> movie = movieRepository.findById(id);
    if (movie.isPresent()) {
      return movie.get();
    } else {
      throw new MovieNotFoundException(id);
    }
  }

  @Override
  public ResponseEntity<List<Movie>> getAllMovies() {
    try {

      List<Movie> movieList = new ArrayList<>(movieRepository.findAll());

      if (movieList.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(movieList, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<Movie> updateMovie(Long id, Movie movie) {
    try {
      Optional<Movie> movieData = movieRepository.findById(id);
      if (movieData.isPresent()) {
        Movie updatedMovie = movieData.get();
        updatedMovie.setTitle(movie.getTitle());

        return new ResponseEntity<>(movieRepository.save(updatedMovie), HttpStatus.CREATED);
      }

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void deleteMovie(Long id) throws MovieNotFoundException {
    movieRepository.deleteById(id);
  }
}
