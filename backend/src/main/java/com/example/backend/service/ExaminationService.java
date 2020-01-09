package com.example.backend.service;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.model.Examination;

public interface ExaminationService {

    Examination createExamination(String username, ExaminationRequestDTO examinatioRequestDTO);
}
