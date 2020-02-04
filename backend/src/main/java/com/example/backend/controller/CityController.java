package com.example.backend.controller;

import com.example.backend.dto.response.CityResponseDTO;
import com.example.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping()
    public ResponseEntity<List<CityResponseDTO>> getCities() {
        return ResponseEntity.ok(cityService.getCities()
                .stream()
                .map(CityResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<List<CityResponseDTO>> getCitiesByCountryId(@PathVariable("countryId") Integer countryId) {
        return ResponseEntity.ok(cityService.getCitiesByCountryId(countryId)
                .stream()
                .map(CityResponseDTO::new)
                .collect(Collectors.toList()));
    }
}
