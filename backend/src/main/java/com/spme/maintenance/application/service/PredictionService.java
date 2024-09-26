package com.spme.maintenance.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.spme.maintenance.domain.model.Prediction;
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
import java.util.List;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;

import javax.xml.parsers.ParserConfigurationException;
import jakarta.xml.bind.JAXBException;



@Service
public class PredictionService {
    private final PredictionRepository predictionRepository;
    private final Evaluator evaluator;

    @Autowired
    public PredictionService(PredictionRepository predictionRepository) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        this.predictionRepository = predictionRepository;
        String currentDirectory = System.getProperty("user.dir");
        
        // Imprimir la ruta del directorio actual
        System.out.println("Directorio actual: " + currentDirectory);
        this.evaluator = new LoadingModelEvaluatorBuilder()
                        .load(new FileInputStream("./backend/src/main/resources/models/predictor.pmml"))
                    .build();
        this.evaluator.verify();
        System.out.println("Modelo cargado exitosamente");
    }

    public Prediction execPrediction(Map<String, Object> measurements) {
        
        Map<String, FieldValue> measurementMap = getMeasurementsPrepared(measurements);

        Map<String, ?> results = evaluator.evaluate(measurementMap);
        
        // Extraer el resultado de la predicción
        results = EvaluatorUtil.decodeAll(results);

        // Crear y guardar la predicción
        Prediction prediction = new Prediction();
        prediction.setEquipmentId(measurements.get("equipmentId").toString());
        prediction.setRecordDate(LocalDateTime.now());
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

        return predictionRepository.save(prediction);
        
    }

    private Map<String, FieldValue> getMeasurementsPrepared(Map<String, Object> measurements) {
        List<InputField> inputFields = evaluator.getInputFields();
        Map<String, FieldValue> arguments = new LinkedHashMap<>();
        for(InputField inputField : inputFields){
            String inputName = inputField.getName();
            Object rawValue = measurements.get(inputName);
            FieldValue inputValue = inputField.prepare(rawValue);
            arguments.put(inputName, inputValue);
        }
        return arguments;
    }

}
