package com.example.backend.service;

import com.example.backend.model.Examination;

public interface EmailService {

    void sendConfirmationMailToUser(Integer id);

    void sendConfirmationMailToPatient(Examination examination);

    void sendConfirmationMailToDoctor(Integer examinationId);
}
