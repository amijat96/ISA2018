package com.example.backend.service;

import com.example.backend.model.Country;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CountryService {

    List<Country> getCountries();

}
