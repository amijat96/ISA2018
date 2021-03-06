package com.example.backend.service;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.model.Examination;

public interface ExaminationService {

    Examination createExamination(String username, ExaminationRequestDTO examinatioRequestDTO);

    Examination approveExamination(Integer id, ExaminationRequestDTO examinationRequestDTO);

    Integer confirmExamination(String token);

    Examination getExamination(Integer id);

    Examination cancelExamination(Integer id);
}
