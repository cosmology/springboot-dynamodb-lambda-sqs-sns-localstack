package com.ivanprokic.ticketproducer.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;

@Component
@AutoConfiguration(after = ServerHttpObservationFilter.class)
public class LogObservationHandler implements ObservationHandler<Observation.Context> {

  private static final Logger log = LoggerFactory.getLogger(LogObservationHandler.class);

  @Override
  public void onStart(Observation.Context context) {
    log.info("LogObservationHandler::onStart: context {}", context);
  }

  @Override
  public void onStop(Observation.Context context) {
    log.info("LogObservationHandler::onStop: {}", context.getName());
  }

  @Override
  public boolean supportsContext(Observation.Context context) {
    return true;
  }
}
