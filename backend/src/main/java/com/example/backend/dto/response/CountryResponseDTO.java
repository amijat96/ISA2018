package com.example.backend.dto.response;

import com.example.backend.model.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponseDTO {

    private Integer countryId;

    private String name;

    public CountryResponseDTO(Country country) {
        this.countryId = country.getCountryId();
        this.name = country.getName();
    }
}
