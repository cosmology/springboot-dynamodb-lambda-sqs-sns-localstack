package com.ivanprokic.movieticketconsumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ivanprokic.movieticketconsumer.entity.Movie;
import com.ivanprokic.movieticketconsumer.exception.MovieNotFoundException;
import com.ivanprokic.movieticketconsumer.repository.MovieRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MoiveService {

    MovieRepository movieRepository;
    
    @Override
    public Movie creatMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()){
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
