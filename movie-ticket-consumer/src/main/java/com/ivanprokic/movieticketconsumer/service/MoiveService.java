package com.ivanprokic.movieticketconsumer.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ivanprokic.movieticketconsumer.entity.Movie;
import com.ivanprokic.movieticketconsumer.exception.MovieNotFoundException;

public interface MoiveService {

    Movie creatMovie (Movie movie);
    Movie getMovieById(Long id) throws MovieNotFoundException;
    ResponseEntity<List<Movie>> getAllMovies();
    ResponseEntity<Movie> updateMovie(Long id, Movie movie);
    void deleteMovie(Long id) throws MovieNotFoundException;


}
