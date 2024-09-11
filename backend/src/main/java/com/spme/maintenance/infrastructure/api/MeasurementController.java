package com.spme.maintenance.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spme.maintenance.application.service.MeasurementService;
import com.spme.maintenance.domain.model.Measurement;
import org.springframework.http.HttpStatus;

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
    
}
