package com.example.backend.service.impl;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.exception.ExaminationNotFoundException;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.DeletionException;
import com.example.backend.model.City;
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
    public Clinic getClinic(Integer id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Clinic createClinic(ClinicRequestDTO clinicRequestDTO) {
        Clinic clinic = new Clinic();
        clinic.setCity(cityRepository.findById(clinicRequestDTO.getCityId())
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find city with given id")));
        clinic.setName(clinicRequestDTO.getName());
        clinic.setDescription(clinicRequestDTO.getDescription());
        clinic.setStreet(clinicRequestDTO.getStreet());
        clinic.setWorkTimeStart(clinicRequestDTO.getWorkTimeStart());
        clinic.setWorkTimeEnd(clinicRequestDTO.getWorkTimeEnd());
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Clinic updateClinic(Integer id, ClinicRequestDTO clinicRequestDTO) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id."));
        clinic.setName(clinicRequestDTO.getName());
        clinic.setWorkTimeStart(clinicRequestDTO.getWorkTimeStart());
        clinic.setWorkTimeEnd(clinicRequestDTO.getWorkTimeEnd());
        clinic.setStreet(clinicRequestDTO.getStreet());
        clinic.setDescription(clinicRequestDTO.getDescription());
        City city = cityRepository.findById(clinicRequestDTO.getCityId())
                .orElseThrow(() -> new ExaminationNotFoundException("Could not fin dcity with given id."));
        clinic.setCity(city);
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    public boolean deleteClinic(Integer id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id."));
        if (clinic.getUsers().size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        if(clinic.getRooms().size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        if(clinic.getPriceLists().size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        try {
            clinicRepository.delete(clinic);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
