package com.spme.maintenance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.spme.maintenance.application.service.MeasurementService;
import com.spme.maintenance.application.service.PredictionService;
import com.spme.maintenance.application.service.MeasurementObserver;


@Configuration
public class ApplicationConfig {

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private PredictionService predictionService;


    @Bean
    public MeasurementObserver configureMeasurementObserver() {
        measurementService.addObserver(predictionService);
        return predictionService;
    }
}
