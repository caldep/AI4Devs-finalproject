package com.spme.maintenance.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.spme.maintenance.application.service.PredictionService;
import com.spme.maintenance.domain.model.Prediction;
import java.util.logging.Logger;
import java.util.logging.Level;
@RestController
@RequestMapping("/predictions")
public class PredictionController {
    private final PredictionService predictionService;

    @Autowired
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public ResponseEntity<Prediction> execPrediction(@RequestBody Map<String, Object> measurements) {
        try {
            Prediction prediction = predictionService.execPrediction(measurements);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            // Registrar el error
            Logger.getLogger(PredictionController.class.getName()).log(Level.SEVERE, "Error al realizar la predicción", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Prediction>> getPredictions(
            @RequestParam String equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (ChronoUnit.DAYS.between(startDate, endDate) > 365) {
            return ResponseEntity.badRequest().build();
        }
        List<Prediction> predictions = predictionService.getPredictions(equipmentId, startDate, endDate);
        if (predictions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(predictions);
    }
}