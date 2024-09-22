package com.spme.maintenance.infrastructure.api;

import com.spme.maintenance.application.service.EquipmentService;
import com.spme.maintenance.domain.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<String> createEquipment(@Valid @RequestBody Equipment equipment) {
        String equipmentId = equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(equipmentId, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<String>> createMultipleEquipments(@Valid @RequestBody List<Equipment> equipments) {
        List<String> equipmentIds = equipmentService.createMultipleEquipments(equipments);
        return new ResponseEntity<>(equipmentIds, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // Implementar otros métodos CRUD aquí
}
