package com.spme.maintenance.domain.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@DynamoDBTable(tableName = "Equipment")
public class Equipment {
    @Id
    @DynamoDBHashKey
    private String equipmentId;

    @NotBlank(message = "El nombre es obligatorio")
    @DynamoDBAttribute
    private String name;

    @NotBlank(message = "El tipo es obligatorio")
    @DynamoDBAttribute
    private String type;

    @NotBlank(message = "La ubicación es obligatoria")
    @DynamoDBAttribute
    private String location;

    @NotNull(message = "La fecha de instalación es obligatoria")
    @Past(message = "La fecha de instalación debe ser en el pasado")
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBAttribute
    private LocalDate installationDate;

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
