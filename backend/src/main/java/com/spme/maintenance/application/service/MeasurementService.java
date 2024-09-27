package com.spme.maintenance.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spme.maintenance.domain.model.Measurement;
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

    public Measurement saveMeasurement(Measurement measurement) {
        Measurement savedMeasurement = measurementRepository.save(measurement);
        notifyObservers(savedMeasurement);
        return savedMeasurement;
    }

    private void notifyObservers(Measurement measurement) {
        for (MeasurementObserver observer : observers) {
            try {
                observer.onMeasurementSaved(measurement);
            } catch (Exception e) {
                // Registrar el error pero continuar con los demás observadores
                Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, 
                    "Error al notificar al observador: " + observer.getClass().getSimpleName(), e);
            }
        }
    }

    public List<Measurement> getSensorData(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Measurement> measurements = new ArrayList<>(measurementRepository.findByEquipmentIdAndRegistrationDateBetween(equipmentId, startDate, endDate));
        measurements.sort(Comparator.comparing(Measurement::getRegistrationDate));
        return measurements;
    }

}