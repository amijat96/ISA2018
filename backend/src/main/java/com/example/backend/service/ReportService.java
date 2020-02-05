package com.example.backend.service;

import com.example.backend.dto.request.ReportRequestDTO;
import com.example.backend.model.Report;

public interface ReportService{

    Report findReportById(Integer id);

    Report createReport(ReportRequestDTO reportRequestDTO);
}
