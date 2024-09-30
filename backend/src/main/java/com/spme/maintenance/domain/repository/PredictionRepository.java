package com.spme.maintenance.domain.repository;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.spme.maintenance.domain.model.Prediction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

public interface PredictionRepository extends CrudRepository<Prediction, String> {
    @NonNull
    <S extends Prediction> S save(@NonNull S prediction);
    @NonNull
    Optional<Prediction> findById(@NonNull String predictionId);
    List<Prediction> findByEquipmentId(String equipmentId);
    List<Prediction> findByEquipmentIdAndRecordDateBetween(String equipmentId, LocalDateTime startDate, LocalDateTime endDate);
}