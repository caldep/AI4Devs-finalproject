package com.spme.maintenance.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spme.maintenance.application.dto.MeasurementWithPredictionDTO;
import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.model.Prediction;
import com.spme.maintenance.domain.repository.MeasurementRepository;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final List<MeasurementObserver> observers = new ArrayList<>();

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void addObserver(MeasurementObserver observer) {
        observers.add(observer);
    }

    public CompletableFuture<MeasurementWithPredictionDTO> saveMeasurement(Measurement measurement) {
        Measurement savedMeasurement = measurementRepository.save(measurement);
        
        return CompletableFuture.supplyAsync(() -> {
            Prediction prediction = notifyObserversAndGetPrediction(savedMeasurement);
            return new MeasurementWithPredictionDTO(
                savedMeasurement,
                prediction.getPredictiveEventType(),
                prediction.getProbability()
            );
        });
    }

    private Prediction notifyObserversAndGetPrediction(Measurement measurement) {
        Prediction prediction = null;
        for (MeasurementObserver observer : observers) {
            try {
                if (observer instanceof PredictionService) {
                    prediction = ((PredictionService) observer).onMeasurementSaved(measurement);
                } else {
                    observer.onMeasurementSaved(measurement);
                }
            } catch (Exception e) {
                Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, 
                    "Error al notificar al observador: " + observer.getClass().getSimpleName(), e);
            }
        }
        return prediction;
    }

    public List<Measurement> getSensorData(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Measurement> measurements = new ArrayList<>(measurementRepository.findByEquipmentIdAndRegistrationDateBetween(equipmentId, startDate, endDate));
        measurements.sort(Comparator.comparing(Measurement::getRegistrationDate));
        return measurements;
    }

}