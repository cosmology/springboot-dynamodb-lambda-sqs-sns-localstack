package com.ivanprokic.movieticketconsumer.config;

// import io.micrometer.core.instrument.Tag;
// import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsFilterConfig {

  //    @Bean
  //    public MeterFilter commonTagsMeterFilter() {
  //        return MeterFilter.commonTags(
  //                List.of(
  //                        Tag.of("instance.uuid", UUID.randomUUID().toString()),
  //                        Tag.of("zone.id", ZoneId.of("America/Los_Angeles").toString())
  //                )
  //        );
  //    }
}
