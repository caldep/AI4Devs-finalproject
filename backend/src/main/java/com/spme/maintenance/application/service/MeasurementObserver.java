package com.spme.maintenance.application.service;

import com.spme.maintenance.domain.model.Measurement;

public interface MeasurementObserver {
    void onMeasurementSaved(Measurement measurement);
}
