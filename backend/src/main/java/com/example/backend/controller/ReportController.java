package com.example.backend.controller;

import com.example.backend.dto.request.ReportRequestDTO;
import com.example.backend.dto.response.ReportResponseDTO;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/reports")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ReportResponseDTO> getReport(@PathVariable Integer id) {
        return ResponseEntity.ok(new ReportResponseDTO(reportService.findReportById(id)));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<ReportResponseDTO> createResponse(@PathVariable Integer id, @Valid @RequestBody ReportRequestDTO reportRequestDTO) {
        return ResponseEntity.ok(new ReportResponseDTO(reportService.createReport(id, reportRequestDTO)));
    }
}
