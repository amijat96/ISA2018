package com.example.backend.service.impl;

import com.example.backend.model.Medicine;
import com.example.backend.repository.MedicineRepository;
import com.example.backend.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {
    private MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository){
        this.medicineRepository = medicineRepository;
    }
    @Override
    public List<Medicine> getMedicines() {
        return medicineRepository.findAll()
                .stream()
                .filter(m -> !m.isDeleted())
                .collect(Collectors.toList());
    }
}
