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
      Measurement measurement = new Measurement();
      measurement.setId(UUID.randomUUID().toString()); // Generar un ID único
      measurement.setEquipmentId("EQUIPO1");
      LocalDateTime now = LocalDateTime.now();
      measurement.setRegistrationDate(now);
      measurement.setFrequency(50.0);
      measurement.setCurrent(10.0);
      measurement.setInternalPressure(2.5);
      measurement.setExternalPressure(3.0);
      measurement.setInternalTemperature(60.0);
      measurement.setExternalTemperature(65.0);
      measurement.setVibrationX(0.5);

      Measurement savedMeasurement = measurementRepository.save(measurement);

      assertNotNull(savedMeasurement);
      assertNotNull(savedMeasurement.getId());
      assertEquals("EQUIPO1", savedMeasurement.getEquipmentId());
      assertEquals(now, savedMeasurement.getRegistrationDate());
      assertEquals(50.0, savedMeasurement.getFrequency(), 0.001);
      assertEquals(10.0, savedMeasurement.getCurrent(), 0.001);
      assertEquals(2.5, savedMeasurement.getInternalPressure(), 0.001);
      assertEquals(3.0, savedMeasurement.getExternalPressure(), 0.001);
      assertEquals(60.0, savedMeasurement.getInternalTemperature(), 0.001);
      assertEquals(65.0, savedMeasurement.getExternalTemperature(), 0.001);
      assertEquals(0.5, savedMeasurement.getVibrationX(), 0.001);

      // Verificar que se puede recuperar de la base de datos
      Optional<Measurement> retrievedMeasurement = measurementRepository.findById(savedMeasurement.getId());
      assertTrue(retrievedMeasurement.isPresent());
      assertEquals(savedMeasurement.getEquipmentId(), retrievedMeasurement.get().getEquipmentId());
      assertEquals(savedMeasurement.getRegistrationDate(), retrievedMeasurement.get().getRegistrationDate());
      
  }
  
}
