package com.example.backend.dto.response;

import com.example.backend.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityResponseDTO {

    private Integer cityId;

    private String name;

    private Integer countryId;

    public CityResponseDTO(City city) {
        this.cityId = city.getCityId();
        this.name = city.getName();
        this.countryId = city.getCountry().getCountryId();
    }
}
