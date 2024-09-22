package com.spme.maintenance.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.spme.maintenance.application.service.MeasurementService;
import com.spme.maintenance.domain.model.Measurement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    @Autowired
    private MeasurementService measurementService;
    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMeasurement(@RequestBody Measurement measurement) {
        measurementService.saveMeasurement(measurement);
    }

    @GetMapping
    public ResponseEntity<List<Measurement>> getSensorData(
            @RequestParam String equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Measurement> measurements = measurementService.getSensorData(equipmentId, startDate, endDate);
        return ResponseEntity.ok(measurements);
    }

    
    
}
