package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListRequestDTO {

    private Integer clinicId;

    private Integer typeOfExaminationId;

    private double price;
}
