package com.spme.maintenance.infrastructure.api;

import com.spme.maintenance.application.service.EquipmentService;
import com.spme.maintenance.domain.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/equipments")
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

    @GetMapping
    public ResponseEntity<?> getAllEquipments(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        try {
            List<Equipment> equipments = equipmentService.getAllEquipments(type, location, page, size, sortBy);
            
            Map<String, Object> response = new HashMap<>();
            response.put("equipments", equipments);
            response.put("currentPage", page);
            response.put("pageSize", size);
            Map<String, String> filters = new HashMap<>();
            if (type != null) {
                filters.put("type", type);
            }
            if (location != null) {
                filters.put("location", location);
            }
            response.put("filters", filters);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener equipos");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Implementar otros métodos CRUD aquí
}
