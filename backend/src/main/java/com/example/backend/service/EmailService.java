package com.example.backend.service;

import com.example.backend.model.Examination;

public interface EmailService {

    void sendConfirmationMailToUser(int id);

    void sendConfirmationMailToPatient(Examination examination);
}
