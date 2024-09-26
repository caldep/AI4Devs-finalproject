package com.spme.maintenance.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public List<Measurement> getSensorData(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Measurement> measurements = new ArrayList<>(measurementRepository.findByEquipmentIdAndRegistrationDateBetween(equipmentId, startDate, endDate));
        measurements.sort(Comparator.comparing(Measurement::getRegistrationDate));
        return measurements;
    }

}