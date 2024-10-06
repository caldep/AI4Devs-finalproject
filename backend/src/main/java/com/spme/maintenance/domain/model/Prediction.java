package com.spme.maintenance.domain.model;



import org.springframework.data.annotation.Id;
import lombok.Data;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "Prediction")
public class Prediction {
    @JsonProperty("id")
    @Id
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @JsonProperty("equipmentId")
    @DynamoDBAttribute
    private String equipmentId;

    @JsonProperty("recordDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute
    private LocalDateTime recordDate;

    @JsonProperty("predictiveEventType")
    @DynamoDBAttribute
    private String predictiveEventType;

    @JsonProperty("probability")
    @DynamoDBAttribute
    private Double probability;

    @JsonProperty("frequency")
    @DynamoDBAttribute
    private Double frequency;

    @JsonProperty("current")
    @DynamoDBAttribute
    private Double current;

    @JsonProperty("pressureIn")
    @DynamoDBAttribute
    private Double pressureIn;

    @JsonProperty("pressureOut")
    @DynamoDBAttribute
    private Double pressureOut;

    @JsonProperty("temperatureIn")
    @DynamoDBAttribute
    private Double temperatureIn;

    @JsonProperty("temperatureOut")
    @DynamoDBAttribute
    private Double temperatureOut;

    @JsonProperty("vibrationX")
    @DynamoDBAttribute
    private Double vibrationX;

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
