package com.spme.maintenance.domain.validation;

import com.spme.maintenance.domain.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingEquipmentValidator implements ConstraintValidator<ExistingEquipment, String> {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public boolean isValid(String equipmentId, ConstraintValidatorContext context) {
        if (equipmentId == null) {
            return false;
        }
        return equipmentRepository.existsByEquipmentId(equipmentId);
    }
}
