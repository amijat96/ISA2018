package com.example.backend.service;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.dto.request.ClinicReportRequestDTO;
import com.example.backend.dto.response.ClinicReportResponseDTO;
import com.example.backend.model.Clinic;
import com.example.backend.model.Examination;
import com.example.backend.model.User;
import org.joda.time.LocalDate;

import java.util.List;

public interface ClinicService {

    List<Clinic> getClinics();

    Clinic getClinic(Integer id);

    Clinic createClinic(ClinicRequestDTO clinicRequestDTO);

    Clinic updateClinic(Integer id, ClinicRequestDTO clinicRequestDTO);

    boolean deleteClinic(Integer id);

    List<User> getClinicPatients(Integer id);

    double getClinicRevenues(Integer clinicId, LocalDate startDate, LocalDate endDate);

    List<Examination> getAllClinicExaminations(Integer clinicId);

    double getClinicGrade(Integer clinicId);

    ClinicReportResponseDTO getReport(Integer id, ClinicReportRequestDTO clinicReportRequestDTO);
}
