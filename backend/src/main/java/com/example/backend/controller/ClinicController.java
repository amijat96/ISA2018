package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.dto.request.ClinicReportRequestDTO;
import com.example.backend.dto.response.ClinicResponseDTO;
import com.example.backend.dto.response.ExaminationResponseDTO;
import com.example.backend.dto.response.ClinicReportResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_SYSTEM')")
    public ResponseEntity<List<ClinicResponseDTO>> getClinics()
    {
        return ResponseEntity.ok(clinicService.getClinics()
            .stream()
            .map(ClinicResponseDTO::new)
            .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ClinicResponseDTO> getClinic(@PathVariable Integer id) {
        return ResponseEntity.ok(new ClinicResponseDTO(clinicService.getClinic(id)));
    }

    @GetMapping(path = "/{id}/grade")
    public double getClinicGrade(@PathVariable Integer id) {
        return clinicService.getClinicGrade(id);
    }

    @PutMapping(path = "/{id}/report")
    public ResponseEntity<ClinicReportResponseDTO> getClinicReport(@PathVariable Integer id, @Valid @RequestBody ClinicReportRequestDTO clinicReportRequestDTO) {
        return ResponseEntity.ok(clinicService.getReport(id, clinicReportRequestDTO));
    }

    @GetMapping(path = "/{id}/patients")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<UserResponseDTO>> getClinicPatients(@PathVariable Integer id) {
        return ResponseEntity.ok(clinicService.getClinicPatients(id)
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_SYSTEM')")
    public ResponseEntity<ClinicResponseDTO> createClinic(@Valid @RequestBody ClinicRequestDTO clinicRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClinicResponseDTO(clinicService.createClinic(clinicRequestDTO)));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<ClinicResponseDTO> updateClinic(@PathVariable Integer id, @Valid @RequestBody ClinicRequestDTO clinicRequestDTO) {
        return ResponseEntity.ok(new ClinicResponseDTO(clinicService.updateClinic(id, clinicRequestDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC') or hasRole('ROLE_ADMIN_SYSTEM')")
    public ResponseEntity<ApiResponse> deleteClinic(@PathVariable Integer id) {
        if(clinicService.deleteClinic(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "Clinic deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete clinic with given id.", new ArrayList<>()));
        }
    }

    @GetMapping(path = "/{id}/examinations")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<List<ExaminationResponseDTO>> getClinicExaminations(@PathVariable Integer id) {
        return ResponseEntity.ok(clinicService.getAllClinicExaminations(id)
                .stream()
                .map(ExaminationResponseDTO::new)
                .collect(Collectors.toList()));
    }

}
