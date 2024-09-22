package com.spme.maintenance.application.service;

import com.spme.maintenance.domain.model.Equipment;
import com.spme.maintenance.domain.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;


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

    public List<Equipment> getAllEquipments(String filterType, String filterLocation, int page, int size, String sortBy) {
        try {
            List<Equipment> equipments = new ArrayList<>();
    equipmentRepository.findAll().forEach(equipments::add);
    
    if (filterType != null && !filterType.isEmpty()) {
        equipments = equipments.stream()
            .filter(e -> e.getType().equalsIgnoreCase(filterType))
            .collect(Collectors.toList());
    }
    
    if (filterLocation != null && !filterLocation.isEmpty()) {
        equipments = equipments.stream()
            .filter(e -> e.getLocation().equalsIgnoreCase(filterLocation))
            .collect(Collectors.toList());
    }
    
    equipments.sort(Comparator.comparing(e -> {
        switch (sortBy) {
            case "name":
                return e.getName();
            case "type":
                return e.getType();
            case "location":
                return e.getLocation();
            default:
                return e.getName();
        }
    }));
    
    int start = page * size;
    int end = Math.min((page + 1) * size, equipments.size());
    return equipments.subList(start, end);
        
            
    
        } catch (Exception e) {
            // Log detallado del error
            e.printStackTrace();
            throw new RuntimeException("Error al obtener equipos: " + e.getMessage(), e);
        }
    }

    // Implementar otros métodos CRUD aquí
}
