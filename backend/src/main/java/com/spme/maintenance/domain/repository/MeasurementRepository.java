package com.spme.maintenance.domain.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.spme.maintenance.domain.model.Measurement;
import java.time.LocalDateTime;
import java.util.List;

@EnableScan
@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, String> {
    List<Measurement> findByEquipmentIdAndRegistrationDateBetween(
        String equipmentId, LocalDateTime startDate, LocalDateTime endDate);
}
