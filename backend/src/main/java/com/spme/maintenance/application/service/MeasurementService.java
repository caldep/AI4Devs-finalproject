package com.spme.maintenance.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spme.maintenance.application.dto.MeasurementGraphicsDTO;
import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.model.Prediction;
import com.spme.maintenance.domain.repository.MeasurementRepository;
import com.spme.maintenance.domain.repository.PredictionRepository;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final List<MeasurementObserver> observers = new ArrayList<>();
    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void addObserver(MeasurementObserver observer) {
        observers.add(observer);
    }

    public CompletableFuture<MeasurementGraphicsDTO> saveMeasurement(Measurement measurement) {
        Measurement savedMeasurement = measurementRepository.save(measurement);
        
        return CompletableFuture.supplyAsync(() -> {
            Prediction prediction = notifyObserversAndGetPrediction(savedMeasurement);
            if (prediction != null) {
                return new MeasurementGraphicsDTO(
                    savedMeasurement,
                    prediction.getPredictiveEventType(),
                    prediction.getProbability()
                );
            } else {
                // Manejar el caso en que no se pudo obtener una predicción
                return new MeasurementGraphicsDTO(
                    savedMeasurement,
                    "-1",
                    1.0
                );
            }
        });
    }

    private Prediction notifyObserversAndGetPrediction(Measurement measurement) {
        Prediction prediction = null;
        for (MeasurementObserver observer : observers) {
            try {
                if (observer instanceof PredictionService) {
                    prediction = ((PredictionService) observer).onMeasurementSaved(measurement);
                    if (prediction != null) {
                        break;  // Si obtenemos una predicción válida, salimos del bucle
                    }
                } else {
                    observer.onMeasurementSaved(measurement);
                }
            } catch (Exception e) {
                Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, 
                    "Error al notificar al observador: " + observer.getClass().getSimpleName(), e);
            }
        }
        
        if (prediction == null) {
            // Si no se pudo obtener una predicción, creamos una predicción por defecto
            prediction = new Prediction();
            prediction.setPredictiveEventType("-1");
            prediction.setProbability(1.0);
        }
        
        return prediction;
    }

    public List<Measurement> getSensorData(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Measurement> measurements = new ArrayList<>(measurementRepository.findByEquipmentIdAndRegistrationDateBetween(equipmentId, startDate, endDate));
        measurements.sort(Comparator.comparing(Measurement::getRegistrationDate));
        return measurements;
    }

    public List<MeasurementGraphicsDTO> getSensorDataWithPredictions(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        
        List<Measurement> measurements = getSensorData(equipmentId, startDate, endDate);
        
        List<Prediction> predictions = predictionRepository.findByEquipmentIdAndRecordDateBetween(equipmentId, startDate, endDate);
        
        Map<LocalDateTime, Prediction> predictionMap = predictions.stream()
            .collect(Collectors.toMap(Prediction::getRecordDate, p -> p, (existing, replacement) -> existing));

        List<MeasurementGraphicsDTO> result = measurements.stream()
            .map(m -> {
                Prediction p = predictionMap.getOrDefault(m.getRegistrationDate(), null);
                return new MeasurementGraphicsDTO(
                    m,
                    p != null ? p.getPredictiveEventType() : "-1",
                    p != null ? p.getProbability() : 1.0
                );
            })
            .sorted(Comparator.comparing(MeasurementGraphicsDTO::getRegistrationDate))
            .collect(Collectors.toList());

        return result;
    }

}