package com.example.backend.service.impl;

import com.example.backend.model.City;
import com.example.backend.repository.CityRepository;
import com.example.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getCities() {
        return cityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getCitiesByCountryId(Integer countryId) {
        return cityRepository.findAllByCountryId(countryId);
    }

}
