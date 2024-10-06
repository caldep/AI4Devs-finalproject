package com.spme.maintenance.application.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class TypeSensorDTO {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("min")
    private Double min;
    
    @JsonProperty("max")
    private Double max;
    
    @JsonProperty("limitDown")
    private Double limitDown;
    
    @JsonProperty("limitUp")
    private Double limitUp;    
}
