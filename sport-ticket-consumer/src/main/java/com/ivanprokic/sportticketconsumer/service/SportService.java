package com.ivanprokic.sportticketconsumer.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ivanprokic.sportticketconsumer.entity.Sport;
import com.ivanprokic.sportticketconsumer.exception.SportNotFoundException;

public interface SportService {

    Sport createSport (Sport sport);
    Sport getSportById(Long id) throws SportNotFoundException;
    ResponseEntity<List<Sport>> getAllSports();
    ResponseEntity<Sport> updateSport(Long id, Sport sport);
    void deleteSport(Long id) throws SportNotFoundException;

}
