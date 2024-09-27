package com.spme.maintenance;
 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.repository.MeasurementRepository;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = MaintenanceApplication.class)
public class MeasurementIntegrationTest {
    @Autowired
    private MeasurementRepository measurementRepository;
    

  @Test
  public void testSaveMeasurement() {
    double randomFrequency = 30.0 + Math.random()*40.0;
    double randomCurrent = 300.0 + Math.random()*120.0;
    double randomInternalPressure = 430.0 + Math.random()*70.0;
    double randomExternalPressure = 3000.0 + Math.random()*600.0;
    double randomInternalTemperature = 180.0 + Math.random()*50.0;
    double randomExternalTemperature = 200.0 + Math.random()*60.0;
    double randomVibrationX = Math.random()*200.0;
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

      Measurement savedMeasurement = measurementRepository.save(measurement);

      assertNotNull(savedMeasurement);
      assertNotNull(savedMeasurement.getId());
      assertEquals("Equipo1", savedMeasurement.getEquipmentId());
      assertEquals(now, savedMeasurement.getRegistrationDate());
      assertEquals(randomFrequency, savedMeasurement.getFrequency(), 0.001);
      assertEquals(randomCurrent, savedMeasurement.getCurrent(), 0.001);
      assertEquals(randomInternalPressure, savedMeasurement.getInternalPressure(), 0.001);
      assertEquals(randomExternalPressure, savedMeasurement.getExternalPressure(), 0.001);
      assertEquals(randomInternalTemperature, savedMeasurement.getInternalTemperature(), 0.001);
      assertEquals(randomExternalTemperature, savedMeasurement.getExternalTemperature(), 0.001);
      assertEquals(randomVibrationX, savedMeasurement.getVibrationX(), 0.001);

      // Verificar que se puede recuperar de la base de datos
      Optional<Measurement> retrievedMeasurement = measurementRepository.findById(savedMeasurement.getId());
      assertTrue(retrievedMeasurement.isPresent());
      assertEquals(savedMeasurement.getEquipmentId(), retrievedMeasurement.get().getEquipmentId());
      assertEquals(savedMeasurement.getRegistrationDate(), retrievedMeasurement.get().getRegistrationDate());
      
  }
  
}
