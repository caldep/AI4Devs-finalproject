package com.spme.maintenance.application.dto;

import com.spme.maintenance.domain.model.Measurement;
import lombok.Data;

@Data
public class MeasurementWithPredictionDTO {
    private Measurement measurement;
    private String predictiveEventType;
    private Double probability;

    public MeasurementWithPredictionDTO(Measurement measurement, String predictiveEventType, Double probability) {
        this.measurement = measurement;
        this.predictiveEventType = predictiveEventType;
        this.probability = probability;
    }
}
