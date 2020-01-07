package com.example.backend.service.impl;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.exception.CityNotFoundException;
import com.example.backend.model.Clinic;
import com.example.backend.repository.CityRepository;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    private final CityRepository cityRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository clinicRepository, CityRepository cityRepository) {
        this.clinicRepository = clinicRepository;
        this.cityRepository = cityRepository;
    }
    @Override
    public List<Clinic> getClinics() {
        return clinicRepository.findAll();
    }

    @Override
    public Clinic getClinic(int id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Clinic createClinic(ClinicRequestDTO clinicRequestDTO) {
        Clinic clinic = new Clinic();
        clinic.setCity(cityRepository.findById(clinicRequestDTO.getCityId())
                .orElseThrow(() -> new CityNotFoundException("Could not find city with given id")));
        clinic.setName(clinicRequestDTO.getName());
        clinic.setDescription(clinicRequestDTO.getDescription());
        clinic.setStreet(clinicRequestDTO.getStreet());
        clinic.setWorkTimeStart(clinicRequestDTO.getWorkTimeStart());
        clinic.setWorkTimeEnd(clinicRequestDTO.getWorkTimeEnd());
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    public Clinic updateClinic(int id, ClinicRequestDTO clinicRequestDTO) {
        return null;
    }
}
