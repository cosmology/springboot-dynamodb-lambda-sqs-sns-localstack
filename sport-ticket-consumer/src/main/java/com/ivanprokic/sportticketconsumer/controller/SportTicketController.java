package com.ivanprokic.sportticketconsumer.controller;

import com.ivanprokic.sportticketconsumer.entity.Sport;
import com.ivanprokic.sportticketconsumer.service.SportService;
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
@RequestMapping("/api/v1/sport")
public class SportTicketController {

  SportService sportService;

  @GetMapping("")
  public ResponseEntity<List<Sport>> getAllSports() {
    return sportService.getAllSports();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Sport> getSportById(@PathVariable Long id) {
    return new ResponseEntity<>(sportService.getSportById(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Sport> createSport(@RequestBody Sport movie) {
    return new ResponseEntity<>(sportService.createSport(movie), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Sport> updateSport(@PathVariable Long id, @RequestBody Sport movie) {
    return sportService.updateSport(id, movie);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteSport(@PathVariable Long id) {
    sportService.deleteSport(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
