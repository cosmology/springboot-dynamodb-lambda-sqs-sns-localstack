package com.ivanprokic.movieticketconsumer.controller;

import com.ivanprokic.movieticketconsumer.entity.Movie;
import com.ivanprokic.movieticketconsumer.service.MovieService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/movie")
public class MovieTicketController {

  MovieService movieService;

  @GetMapping("")
  public ResponseEntity<List<Movie>> getAllMovies() {
    return movieService.getAllMovies();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
    return new ResponseEntity<>(movieService.getMovieById(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
    return new ResponseEntity<>(movieService.createMovie(movie), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
    return movieService.updateMovie(id, movie);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteMovie(@PathVariable Long id) {
    movieService.deleteMovie(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
