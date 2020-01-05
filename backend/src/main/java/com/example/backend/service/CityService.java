package com.example.backend.service;

import com.example.backend.model.City;
import java.util.List;

public interface CityService {

    List<City> getCities();

    List<City> getCitiesByCountryId(Integer countryId);
}
