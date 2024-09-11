package com.spme.maintenance.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.repository.MeasurementRepository;

@Service
public class MeasurementService {
    @Autowired
    private MeasurementRepository measurementRepository;

    public void saveMeasurement(Measurement measurement) {
        measurementRepository.save(measurement);
    }

}