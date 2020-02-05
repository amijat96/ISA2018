package com.example.backend.controller;

import com.example.backend.dto.response.ReportResponseDTO;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
