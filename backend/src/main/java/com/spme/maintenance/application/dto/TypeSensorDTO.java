package com.spme.maintenance.application.dto;

import lombok.Data;

@Data
public class TypeSensorDTO {
    private String name;
    private Double min;
    private Double max;
    private Double limitDown;
    private Double limitUp;    
}
