package com.spme.maintenance.application.service;

import com.spme.maintenance.domain.model.Measurement;
import com.spme.maintenance.domain.model.Prediction;

public interface MeasurementObserver {
    Prediction onMeasurementSaved(Measurement measurement);
}
