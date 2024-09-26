package com.spme.maintenance.domain.repository;

import com.spme.maintenance.domain.model.Prediction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;
import java.util.List;

public interface PredictionRepository extends CrudRepository<Prediction, String> {
    @NonNull
    <S extends Prediction> S save(@NonNull S prediction);
    @NonNull
    Optional<Prediction> findById(@NonNull String predictionId);
    List<Prediction> findByEquipmentId(String equipmentId);
}