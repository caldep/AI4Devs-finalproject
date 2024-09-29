package com.spme.maintenance;
 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spme.maintenance.application.dto.MeasurementWithPredictionDTO;
import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.repository.MeasurementRepository;
import com.spme.maintenance.application.service.MeasurementService;



import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = MaintenanceApplication.class)
public class MeasurementIntegrationTest {
  @Autowired
  private MeasurementRepository measurementRepository;
  @Autowired
  private MeasurementService measurementService;

  @Test
  public void testSaveMeasurement() {
    double randomFrequency = 40.0 + Math.random()*20.0;
    double randomCurrent = 250.0 + Math.random()*80.0;
    double randomInternalPressure = 500.0 + Math.random()*300.0;
    double randomExternalPressure = 2000.0 + Math.random()*1500.0;
    double randomInternalTemperature = 70.0 + Math.random()*200.0;
    double randomExternalTemperature = 100.0 + Math.random()*150.0;
    double randomVibrationX = Math.random();

    // Crear un nuevo objeto Measurement
      Measurement measurement = new Measurement();
      measurement.setId(UUID.randomUUID().toString()); // Generar un ID único
      measurement.setEquipmentId("Equipo1");
      LocalDateTime now = LocalDateTime.now();
      measurement.setRegistrationDate(now);
      measurement.setFrequency(randomFrequency);
      measurement.setCurrent(randomCurrent);
      measurement.setInternalPressure(randomInternalPressure);
      measurement.setExternalPressure(randomExternalPressure);
      measurement.setInternalTemperature(randomInternalTemperature);
      measurement.setExternalTemperature(randomExternalTemperature);
      measurement.setVibrationX(randomVibrationX);

      MeasurementWithPredictionDTO savedMeasurement = measurementService.saveMeasurement(measurement).join();

      assertNotNull(savedMeasurement);
      assertNotNull(savedMeasurement.getMeasurement().getId());
      assertNotNull(savedMeasurement.getPredictiveEventType());
      assertNotNull(savedMeasurement.getProbability());
      assertEquals("Equipo1", savedMeasurement.getMeasurement().getEquipmentId());
      assertEquals(now, savedMeasurement.getMeasurement().getRegistrationDate());
      assertEquals(randomFrequency, savedMeasurement.getMeasurement().getFrequency(), 0.001);
      assertEquals(randomCurrent, savedMeasurement.getMeasurement().getCurrent(), 0.001);
      assertEquals(randomInternalPressure, savedMeasurement.getMeasurement().getInternalPressure(), 0.001);
      assertEquals(randomExternalPressure, savedMeasurement.getMeasurement().getExternalPressure(), 0.001);
      assertEquals(randomInternalTemperature, savedMeasurement.getMeasurement().getInternalTemperature(), 0.001);
      assertEquals(randomExternalTemperature, savedMeasurement.getMeasurement().getExternalTemperature(), 0.001);
      assertEquals(randomVibrationX, savedMeasurement.getMeasurement().getVibrationX(), 0.001);

      // Verificar que se puede recuperar de la base de datos
      Optional<Measurement> retrievedMeasurement = measurementRepository.findById(savedMeasurement.getMeasurement().getId());
      assertTrue(retrievedMeasurement.isPresent());
      assertEquals(savedMeasurement.getMeasurement().getEquipmentId(), retrievedMeasurement.get().getEquipmentId());
      assertEquals(savedMeasurement.getMeasurement().getRegistrationDate(), retrievedMeasurement.get().getRegistrationDate());
      
  }
  
}
