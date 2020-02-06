package com.example.backend.service.impl;

import com.example.backend.model.Diagnosis;
import com.example.backend.repository.DiagnosisRepository;
import com.example.backend.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public List<Diagnosis> getDiagnoses() {
        return diagnosisRepository.findAll()
                .stream()
                .filter(d -> !d.isDeleted())
                .collect(Collectors.toList());
    }
}
