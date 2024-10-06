package com.spme.maintenance.application.dto;

import java.time.LocalDateTime;
import com.spme.maintenance.domain.model.Measurement;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Data
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementGraphicsDTO {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("equipmentId")
    private String equipmentId;
    
    @JsonProperty("registrationDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registrationDate;
    
    @JsonProperty("frequency")
    private double frequency;
    
    @JsonProperty("current")
    private double current;
    
    @JsonProperty("internalPressure")
    private double internalPressure;
    
    @JsonProperty("externalPressure")
    private double externalPressure;
    
    @JsonProperty("internalTemperature")
    private double internalTemperature;
    
    @JsonProperty("externalTemperature")
    private double externalTemperature;
    
    @JsonProperty("vibrationX")
    private double vibrationX;
    
    @JsonProperty("predictiveEventType")
    private String predictiveEventType;
    
    @JsonProperty("probability")
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
