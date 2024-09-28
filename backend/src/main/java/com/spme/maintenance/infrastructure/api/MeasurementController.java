package com.spme.maintenance.infrastructure.api;

import com.spme.maintenance.application.dto.MeasurementWithPredictionDTO;
import com.spme.maintenance.application.service.MeasurementService;
import com.spme.maintenance.domain.model.Measurement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<MeasurementWithPredictionDTO>> saveMeasurement(@RequestBody Measurement measurement) {
        return measurementService.saveMeasurement(measurement)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.internalServerError().build());
    }
}
