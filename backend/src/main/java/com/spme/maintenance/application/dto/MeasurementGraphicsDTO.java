package com.spme.maintenance.application.dto;
import java.time.LocalDateTime;
import com.spme.maintenance.domain.model.Measurement;
import lombok.Data;

@Data
public class MeasurementGraphicsDTO {
    private String id;
    private String equipmentId;
    private LocalDateTime registrationDate;
    private double frequency;
    private double current;
    private double internalPressure;
    private double externalPressure;
    private double internalTemperature;
    private double externalTemperature;
    private double vibrationX;
    private String predictiveEventType;
    private Double probability;

    public MeasurementGraphicsDTO(Measurement measurement, String predictiveEventType, Double probability) {
        this.id = measurement.getId();
        this.equipmentId = measurement.getEquipmentId();
        this.registrationDate = measurement.getRegistrationDate();
        this.frequency = measurement.getFrequency();
        this.current = measurement.getCurrent();
        this.internalPressure = measurement.getInternalPressure();
        this.externalPressure = measurement.getExternalPressure();
        this.internalTemperature = measurement.getInternalTemperature();
        this.externalTemperature = measurement.getExternalTemperature();
        this.vibrationX = measurement.getVibrationX();
        this.predictiveEventType = predictiveEventType;
        this.probability = probability;
    }
}
