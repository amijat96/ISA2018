package com.example.backend.service;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.model.Examination;

public interface EmailService {

    void sendConfirmationMailToUser(Integer id);

    void sendConfirmationMailToPatient(Examination examination);

    void sendConfirmationMailToDoctor(Integer examinationId);

    void sendVacationApprovedMailToMedicalStaff(VacationRequestDTO vacationRequestDTO);

    void sendVacationDeniedMailToMedicalStaff(VacationRequestDTO vacationRequestDTO);

    void sendExaminationCanceledMail(Examination examination);

}
