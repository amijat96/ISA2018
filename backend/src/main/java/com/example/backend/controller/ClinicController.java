package com.example.backend.controller;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.dto.response.ClinicResponseDTO;
import com.example.backend.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/clinic")
public class ClinicController {

    private final ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    public ResponseEntity<List<ClinicResponseDTO>> getClinics()
    {
        return ResponseEntity.ok(clinicService.getClinics()
            .stream()
            .map(ClinicResponseDTO::new)
            .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<ClinicResponseDTO> createClinic(@Valid @RequestBody ClinicRequestDTO clinicRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClinicResponseDTO(clinicService.createClinic(clinicRequestDTO)));
    }
}
