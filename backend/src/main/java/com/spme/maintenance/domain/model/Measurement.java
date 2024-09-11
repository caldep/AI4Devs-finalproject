package com.spme.maintenance.domain.model;



import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
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

   
}
