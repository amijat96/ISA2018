package com.example.backend.service.impl;

import com.example.backend.dto.request.ReportRequestDTO;
import com.example.backend.exception.ReportNotFoundException;
import com.example.backend.model.Report;
import com.example.backend.repository.ReportRepository;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report findReportById(Integer id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Could not find report with given id."));
    }

    @Override
    public Report createReport(ReportRequestDTO reportRequestDTO) {
        return null;
    }
}
