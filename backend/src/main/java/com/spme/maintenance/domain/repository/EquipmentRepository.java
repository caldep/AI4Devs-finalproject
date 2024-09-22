package com.spme.maintenance.domain.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.spme.maintenance.domain.model.Equipment;

@EnableScan
@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, String> {
    boolean existsByEquipmentId(String equipmentId);
}
