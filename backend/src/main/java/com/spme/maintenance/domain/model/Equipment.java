package com.spme.maintenance.domain.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@DynamoDBTable(tableName = "Equipment")
public class Equipment {
    @JsonProperty("id")
    @Id
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @JsonProperty("equipmentId")
    @NotBlank(message = "El id es obligatorio")
    @DynamoDBAttribute
    private String equipmentId;

    @JsonProperty("name")
    @NotBlank(message = "El nombre es obligatorio")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "NameIndex")
    @DynamoDBAttribute
    private String name;

    @JsonProperty("type")
    @NotBlank(message = "El tipo es obligatorio")
    @DynamoDBAttribute
    private String type;

    @JsonProperty("location")
    @NotBlank(message = "La ubicación es obligatoria")
    @DynamoDBAttribute
    private String location;

    @JsonProperty("installationDate")
    @NotNull(message = "La fecha de instalación es obligatoria")
    @Past(message = "La fecha de instalación debe ser en el pasado")
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DynamoDBAttribute
    private LocalDate installationDate;

    @JsonProperty("operatorEmails")
    @NotEmpty(message = "Se requiere al menos un correo electrónico de operador")
    @DynamoDBAttribute
    private List<String> operatorEmails;

    // Converter para LocalDate
    public static class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {
        @Override
        public String convert(LocalDate date) {
            return date.toString();
        }

        @Override
        public LocalDate unconvert(String s) {
            return LocalDate.parse(s);
        }
    }
}
