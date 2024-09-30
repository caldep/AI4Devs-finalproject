package com.spme.maintenance.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.spme.maintenance.application.dto.TypeSensorDTO;
import com.spme.maintenance.domain.model.Prediction;
import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.repository.DynamoDBPredictionRepository;
import com.spme.maintenance.domain.repository.PredictionRepository;



//import org.glassfish.jaxb.runtime.v2.schemagen.xmlschema.List;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.FieldValue;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;

import javax.xml.parsers.ParserConfigurationException;
import jakarta.xml.bind.JAXBException;



@Service
public class PredictionService implements MeasurementObserver {
    private final DynamoDBPredictionRepository predictionRepository;
    private final Evaluator evaluator;
    private final Map<String, TypeSensorDTO> sensorTypes;
    private final List<InputField> inputFields;

    @Autowired
    public PredictionService(DynamoDBPredictionRepository predictionRepository, 
                             @Value("${prediction.model.path}") String modelPath) 
            throws IOException, ParserConfigurationException, SAXException, JAXBException {
        this.predictionRepository = predictionRepository;
        this.evaluator = new LoadingModelEvaluatorBuilder()
                        .load(new FileInputStream(modelPath))
                        .build();
        this.evaluator.verify();
        this.sensorTypes = getSensorTypes();
        this.inputFields = evaluator.getInputFields();

    }

    public Prediction execPrediction(Map<String, Object> measurements) {
        
        Map<String, ?> modelResult = getModelResult(measurements);
        Prediction prediction = getPredictionEntity(measurements, modelResult);
        return savePredictionEntity(prediction);
        
    }

    private Map<String, ?> getModelResult(Map<String, Object> measurements) {
        Map<String, FieldValue> measurementMap = getMeasurementsPrepared(measurements);

        Map<String, ?> modelResult = evaluator.evaluate(measurementMap);
        
        // Extraer el resultado de la predicción
        return EvaluatorUtil.decodeAll(modelResult);
        
    }

    private Prediction getPredictionEntity(Map<String, Object> measurements, Map<String, ?> results) {
        // Crear y guardar la predicción
        Prediction prediction = new Prediction();
        prediction.setEquipmentId(measurements.get("equipmentId").toString());
        LocalDateTime recordDate = (LocalDateTime) measurements.get("registrationDate");
        System.out.println("recordDate: " + recordDate);
        prediction.setRecordDate(recordDate);
        prediction.setPredictiveEventType(results.get("y").toString());
        prediction.setProbability((Double) results.get("probability(0)") );
        // Establecer los valores de los sensores
        prediction.setFrequency((Double) measurements.get("x1"));
        prediction.setCurrent((Double) measurements.get("x2"));
        prediction.setPressureIn((Double) measurements.get("x3"));
        prediction.setPressureOut((Double) measurements.get("x4"));
        prediction.setTemperatureIn((Double) measurements.get("x5"));
        prediction.setTemperatureOut((Double) measurements.get("x6"));
        prediction.setVibrationX((Double) measurements.get("x7"));

        return prediction;
    }

    private Prediction savePredictionEntity(Prediction prediction) {
        return predictionRepository.save(prediction);
    }

    private Map<String, FieldValue> getMeasurementsPrepared(Map<String, Object> measurements) {
                    
        Map<String, FieldValue> arguments = new LinkedHashMap<>();
        for(InputField inputField : inputFields){
            String inputName = inputField.getName();
            Object rawValue = measurements.get(inputName);
            TypeSensorDTO sensorType = sensorTypes.get(inputName);
            rawValue = normalize((Double)rawValue, sensorType.getMin(), sensorType.getMax());
            FieldValue inputValue = inputField.prepare(rawValue);
            arguments.put(inputName, inputValue);
        }
        return arguments;
    }

    private Map<String, TypeSensorDTO> getSensorTypes() {
        // Crear el mapa con los siete elementos TypeSensor
        Map<String, TypeSensorDTO> sensorTypes = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            String key = "x" + i;
            TypeSensorDTO sensorType = new TypeSensorDTO();
            switch (i) {
                case 1:sensorType.setName("Frequency");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(428.353224043716);
                    sensorType.setLimitDown(40.0);
                    sensorType.setLimitUp(60.0);
                    break;
                case 2:sensorType.setName("Current");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(465.0);
                    sensorType.setLimitDown(377.1);
                    sensorType.setLimitUp(416.8);
                    break;
                case 3:sensorType.setName("PressureIn");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(3738.69995117188);
                    sensorType.setLimitDown(442.4);
                    sensorType.setLimitUp(489.0);
                    break;
                case 4:sensorType.setName("PressureOut");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(4505.8);
                    sensorType.setLimitDown(3104.1);
                    sensorType.setLimitUp(4505.8);
                    break;
                case 5:sensorType.setName("TemperatureIn");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(279.665);
                    sensorType.setLimitDown(195.54);
                    sensorType.setLimitUp(279.665);
                    break;
                case 6:sensorType.setName("TemperatureOut");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(388.7);
                    sensorType.setLimitDown(228.23);
                    sensorType.setLimitUp(252.25);
                    break;
                case 7:sensorType.setName("VibrationX");
                    sensorType.setMin(-1.0);
                    sensorType.setMax(1540.136);
                    sensorType.setLimitDown(0.0);
                    sensorType.setLimitUp(200.0);
                    break;
            }
            sensorTypes.put(key, sensorType);
        }
        return sensorTypes;
    }

    private Double normalize(Double value, Double min, Double max) {
        return value;
        //return (value - min) / (max - min);
    }

    @Override
    public Prediction onMeasurementSaved(Measurement measurement) {
        Map<String, Object> measurementMap = convertMeasurementToMap(measurement);
        return execPrediction(measurementMap);
    }

    private Map<String, Object> convertMeasurementToMap(Measurement measurement) {
        Map<String, Object> map = new HashMap<>();
        map.put("equipmentId", measurement.getEquipmentId());
        map.put("registrationDate", measurement.getRegistrationDate());
        map.put("x1", measurement.getFrequency());
        map.put("x2", measurement.getCurrent());
        map.put("x3", measurement.getInternalPressure());
        map.put("x4", measurement.getExternalPressure());
        map.put("x5", measurement.getInternalTemperature());
        map.put("x6", measurement.getExternalTemperature());
        map.put("x7", measurement.getVibrationX());
        return map;
    }

    public List<Prediction> getPredictions(String equipmentId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Prediction> predictions = new ArrayList<>(predictionRepository.findByEquipmentIdAndRecordDateBetween(equipmentId, startDate, endDate));
        predictions.sort(Comparator.comparing(Prediction::getRecordDate));
        return predictions;
    }

}
