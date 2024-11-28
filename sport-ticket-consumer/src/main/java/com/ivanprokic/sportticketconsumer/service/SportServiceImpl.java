package com.ivanprokic.sportticketconsumer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ivanprokic.sportticketconsumer.entity.Sport;
import com.ivanprokic.sportticketconsumer.exception.SportNotFoundException;
import com.ivanprokic.sportticketconsumer.repository.SportRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SportServiceImpl implements SportService {

    SportRepository sportRepository;

    @Override
    public Sport createSport(Sport sport) {
        return sportRepository.save(sport);
    }


	@Override
	public Sport getSportById(Long id) throws SportNotFoundException {
		Optional<Sport> sport = sportRepository.findById(id);
        if (sport.isPresent()){
            return sport.get();
        } else {
            throw new SportNotFoundException(id);
        }
	}

    @Override
    public ResponseEntity<List<Sport>> getAllSports() {
       try {

            List<Sport> sportList = new ArrayList<>(sportRepository.findAll());

            if (sportList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(sportList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Override
    public ResponseEntity<Sport> updateSport(Long id, Sport sport) {
        try {
            Optional<Sport> sportData = sportRepository.findById(id);
            if (sportData.isPresent()) {
                Sport updatedSport = sportData.get();
                updatedSport.setTitle(sport.getTitle());

                return new ResponseEntity<>(sportRepository.save(updatedSport), HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void deleteSport(Long id) throws SportNotFoundException {
        sportRepository.deleteById(id);
    }

}
