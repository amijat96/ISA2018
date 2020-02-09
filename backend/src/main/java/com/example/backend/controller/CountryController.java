package com.example.backend.controller;

import com.example.backend.dto.response.CountryResponseDTO;
import com.example.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping()
    public ResponseEntity<List<CountryResponseDTO>> getCountries() {
        return ResponseEntity.ok(countryService.getCountries()
                .stream()
                .map(CountryResponseDTO::new)
                .collect(Collectors.toList()));
    }

}
