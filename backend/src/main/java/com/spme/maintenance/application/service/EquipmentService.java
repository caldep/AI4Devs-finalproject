package com.spme.maintenance.application.service;

import com.spme.maintenance.domain.model.Equipment;
import com.spme.maintenance.domain.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public String createEquipment(Equipment equipment) {
        if (equipmentRepository.existsByEquipmentId(equipment.getEquipmentId())) {
            throw new IllegalArgumentException("Equipment with this ID already exists");
        }
        equipment.setEquipmentId(UUID.randomUUID().toString());
        equipmentRepository.save(equipment);
        return equipment.getEquipmentId();
    }

    public List<String> createMultipleEquipments(List<Equipment> equipments) {
        return equipments.stream()
                .map(this::createEquipment)
                .collect(Collectors.toList());
    }

    // Implementar otros métodos CRUD aquí
}
