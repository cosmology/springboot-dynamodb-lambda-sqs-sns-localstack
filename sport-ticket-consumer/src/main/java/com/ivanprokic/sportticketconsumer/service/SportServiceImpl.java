package com.ivanprokic.sportticketconsumer.service;

import com.ivanprokic.sportticketconsumer.entity.Sport;
import com.ivanprokic.sportticketconsumer.exception.SportNotFoundException;
import com.ivanprokic.sportticketconsumer.repository.SportRepository;
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
public class SportServiceImpl implements SportService {

  SportRepository sportRepository;

  @Override
  @Observed(
      name = "post:createSport",
      contextualName = "post:createSport",
      lowCardinalityKeyValues = {"locale", "en-US"})
  public Sport createSport(Sport sport) {
    // Get the current span context
    SpanContext spanContext = Span.current().getSpanContext();

    // Check if the span is valid
    if (spanContext.isValid()) {
      // Get the trace ID
      String traceId = spanContext.getTraceId();
      String spanId = spanContext.getSpanId();

      // Log Trace ID and add it to Ticket
      log.info("SportServiceImpl createSport with traceId: {}, spanId: {}", traceId, spanId);
      sport.setTraceId(traceId);
    }

    return sportRepository.save(sport);
  }

  @Override
  public Sport getSportById(Long id) throws SportNotFoundException {
    Optional<Sport> sport = sportRepository.findById(id);
    if (sport.isPresent()) {
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
