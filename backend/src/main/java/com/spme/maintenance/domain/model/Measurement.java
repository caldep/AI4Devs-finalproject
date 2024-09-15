package com.spme.maintenance.domain.model;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "Measurement")
public class Measurement {
    @Id
    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String equipmentId;

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute
    private LocalDateTime registrationDate;

    @DynamoDBAttribute
    private double frequency;

    @DynamoDBAttribute
    private double current;

    @DynamoDBAttribute
    private double internalPressure;

    @DynamoDBAttribute
    private double externalPressure;

    @DynamoDBAttribute
    private double internalTemperature;

    @DynamoDBAttribute
    private double externalTemperature;

    @DynamoDBAttribute
    private double vibrationX;

    // Converter para LocalDateTime
    public static class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
        @Override
        public String convert(LocalDateTime time) {
            return time.toString();
        }

        @Override
        public LocalDateTime unconvert(String s) {
            return LocalDateTime.parse(s);
        }
    }
}
