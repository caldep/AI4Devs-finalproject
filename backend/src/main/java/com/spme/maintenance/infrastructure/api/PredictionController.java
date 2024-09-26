package com.spme.maintenance.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spme.maintenance.application.service.PredictionService;
import com.spme.maintenance.domain.model.Prediction;
import java.util.Map;
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
}