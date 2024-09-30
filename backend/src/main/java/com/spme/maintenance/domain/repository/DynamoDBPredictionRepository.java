package com.spme.maintenance.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.lang.NonNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.spme.maintenance.domain.model.Prediction;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Map;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.Comparator;
import org.springframework.stereotype.Repository;


@Repository
public class DynamoDBPredictionRepository implements PredictionRepository {
    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public DynamoDBPredictionRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public @NonNull <S extends Prediction> S save(@NonNull S prediction) {
        dynamoDBMapper.save(prediction);
        return prediction;
    }

    @Override
    public @NonNull Optional<Prediction> findById(@NonNull String predictionId) {
        return Optional.ofNullable(dynamoDBMapper.load(Prediction.class, predictionId));
    }

    @Override
    public List<Prediction> findByEquipmentId(String equipmentId) {
        Prediction keyObject = new Prediction();
        keyObject.setEquipmentId(equipmentId);
        DynamoDBQueryExpression<Prediction> queryExpression = new DynamoDBQueryExpression<Prediction>()
            .withHashKeyValues(keyObject)
            .withConsistentRead(false);
        return dynamoDBMapper.query(Prediction.class, queryExpression);
    }

    @Override
    public @NonNull <S extends Prediction> Iterable<S> saveAll(@NonNull Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entities) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    @Override
    public boolean existsById(@NonNull String id) {
        return findById(id).isPresent();
    }

    @Override
    public @NonNull Iterable<Prediction> findAll() {
        return dynamoDBMapper.scan(Prediction.class, new DynamoDBScanExpression());
    }

    @Override
    public @NonNull Iterable<Prediction> findAllById(@NonNull Iterable<String> ids) {
        List<Prediction> results = new ArrayList<>();
        for (String id : ids) {
            findById(id).ifPresent(results::add);
        }
        return results;
    }

    @Override
    public long count() {
        return dynamoDBMapper.count(Prediction.class, new DynamoDBQueryExpression<Prediction>());
    }

    @Override
    public void deleteById(@NonNull String id) {
        findById(id).ifPresent(dynamoDBMapper::delete);
    }

    @Override
    public void delete(@NonNull Prediction entity) {
        dynamoDBMapper.delete(entity);
    }

    @Override
    public void deleteAllById(@NonNull Iterable<? extends String> ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(@NonNull Iterable<? extends Prediction> entities) {
        for (Prediction entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        dynamoDBMapper.batchDelete(findAll());
    }

    @Override
    public List<Prediction> findByEquipmentIdAndRecordDateBetween(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("equipmentId = :equipmentId and recordDate between :startDate and :endDate")
            .withExpressionAttributeValues(Map.of(
                ":equipmentId", new AttributeValue(equipmentId),
                ":startDate", new AttributeValue().withS(startDate.toString()),
                ":endDate", new AttributeValue().withS(endDate.toString())
            ));
        
        List<Prediction> predictions = dynamoDBMapper.scan(Prediction.class, scanExpression);
        return predictions;
    }
}
