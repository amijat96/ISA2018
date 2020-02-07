package com.example.backend.controller;

import com.example.backend.dto.response.DiagnosisResponseDTO;
import com.example.backend.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/diagnoses")
public class DiagnosisController {

    private DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisController(DiagnosisService diagnosisService){
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getDiagnoses()
                .stream()
                .map(DiagnosisResponseDTO::new)
                .collect(Collectors.toList())
        );
    }
}
