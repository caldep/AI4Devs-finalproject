package com.spme.maintenance.infrastructure.api;

import com.spme.maintenance.application.dto.MeasurementGraphicsDTO;
import com.spme.maintenance.application.service.MeasurementService;
import com.spme.maintenance.domain.model.Measurement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public ResponseEntity<List<Measurement>> getSensorData(
            @RequestParam String equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate) {
        List<Measurement> measurements = measurementService.getSensorData(equipmentId, startDate, endDate);
        return ResponseEntity.ok(measurements);
    }            

    @PostMapping
    public ResponseEntity<MeasurementGraphicsDTO> saveMeasurement(@RequestBody Measurement measurement) {
        try {
            MeasurementGraphicsDTO result = measurementService.saveMeasurement(measurement).get();
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            // Registrar el error
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/graphics")
    public ResponseEntity<List<MeasurementGraphicsDTO>> getSensorDataWithPredictions(
            @RequestParam String equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MeasurementGraphicsDTO> measurementGraphics = measurementService.getSensorDataWithPredictions(equipmentId, startDate, endDate);
        return ResponseEntity.ok(measurementGraphics);
    }
    

    
}
