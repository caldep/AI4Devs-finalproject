package com.spme.maintenance.domain.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.spme.maintenance.domain.model.Measurement;

@EnableScan
@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, String> {
    // Los métodos básicos como save(), findAll(), findById(), etc., ya están incluidos en CrudRepository
    // Puedes agregar métodos personalizados si es necesario
}
