package com.example.backend.service;

import com.example.backend.dto.request.TypeOfExaminationRequestDTO;
import com.example.backend.model.TypeOfExamination;

import java.util.List;

public interface TypeOfExaminationService {

    List<TypeOfExamination> getTypesOfExamination();

    TypeOfExamination createTypeOfExamination(TypeOfExaminationRequestDTO typeOfExaminationRequestDTO);

    TypeOfExamination updateTypeOfExamination(Integer id, TypeOfExaminationRequestDTO typeOfExaminationRequestDTO);

    boolean deleteTypeOfExamination(Integer id);
}
