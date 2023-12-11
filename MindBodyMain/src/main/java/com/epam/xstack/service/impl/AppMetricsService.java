package com.epam.xstack.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AppMetricsService {

    private final MeterRegistry meterRegistry;
    private int requests;
    private int errors;

    @PostConstruct
    public void bindJvmMemoryMetrics() {
        new JvmMemoryMetrics().bindTo(meterRegistry);
        try (JvmGcMetrics metrics = new JvmGcMetrics()) {
            metrics.bindTo(meterRegistry);
        }
        new JvmThreadMetrics().bindTo(meterRegistry);
        new ClassLoaderMetrics().bindTo(meterRegistry);
        new ProcessorMetrics().bindTo(meterRegistry);
    }


    public void updateGauge(String gaugeName, int newValue) {
        AtomicInteger gaugeValue = meterRegistry.gauge(gaugeName, new AtomicInteger());
        if (gaugeValue != null) {
            gaugeValue.set(newValue);
        }
    }


    public void addRequestCount() {
        updateGauge("http_server_request_count_total", ++requests);
    }

    public void addErrorCount() {
        updateGauge("http_server_request_error_total", ++errors);
    }

}
