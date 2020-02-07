package com.example.backend.controller;

import com.example.backend.dto.response.MedicineResponseDTO;
import com.example.backend.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/medicines")
public class MedicineController {

    private MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService){
        this.medicineService = medicineService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_NURSE')")
    ResponseEntity<List<MedicineResponseDTO>> getMedicines() {
        return ResponseEntity.ok(medicineService.getMedicines()
                .stream()
                .map(MedicineResponseDTO::new)
                .collect(Collectors.toList())
        );
    }
}
