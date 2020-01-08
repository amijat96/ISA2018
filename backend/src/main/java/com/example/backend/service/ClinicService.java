package com.example.backend.service;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.model.Clinic;

import java.util.List;

public interface ClinicService {

    List<Clinic> getClinics();

    Clinic getClinic(Integer id);

    Clinic createClinic(ClinicRequestDTO clinicRequestDTO);

    Clinic updateClinic(Integer id, ClinicRequestDTO clinicRequestDTO);

    boolean deleteClinic(Integer id);

}
