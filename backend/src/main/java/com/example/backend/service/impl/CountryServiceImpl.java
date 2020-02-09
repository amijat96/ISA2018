package com.example.backend.service.impl;

import com.example.backend.model.Country;
import com.example.backend.repository.CountryRepository;
import com.example.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> getCountries() {
        return countryRepository.findAll();
    }
}
