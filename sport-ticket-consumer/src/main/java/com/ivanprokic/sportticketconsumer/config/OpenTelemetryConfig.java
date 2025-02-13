package com.ivanprokic.sportticketconsumer.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProviderBuilder;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OpenTelemetryConfig {

  @Bean
  OpenTelemetry openTelemetry(
      SdkLoggerProvider sdkLoggerProvider,
      SdkTracerProvider sdkTracerProvider,
      ContextPropagators contextPropagators) {
    OpenTelemetrySdk openTelemetrySdk =
        OpenTelemetrySdk.builder()
            .setLoggerProvider(sdkLoggerProvider)
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.create(B3Propagator.injectingMultiHeaders()))
            .build();
    OpenTelemetryAppender.install(openTelemetrySdk);
    return openTelemetrySdk;
  }

  @Bean
  SdkLoggerProvider otelSdkLoggerProvider(
      Environment environment, ObjectProvider<LogRecordProcessor> logRecordProcessors) {
    Resource springResource =
        Resource.create(
            Attributes.of(
                AttributeKey.stringKey("spring.application.name"), "sport-ticket-consumer"));
    SdkLoggerProviderBuilder builder =
        SdkLoggerProvider.builder().setResource(Resource.getDefault().merge(springResource));
    logRecordProcessors.orderedStream().forEach(builder::addLogRecordProcessor);
    return builder.build();
  }

  @Bean
  LogRecordProcessor otelLogRecordProcessor(Environment environment) {
    return BatchLogRecordProcessor.builder(
            OtlpGrpcLogRecordExporter.builder().setEndpoint("http://otel-collector:4317").build())
        .build();
  }
}
