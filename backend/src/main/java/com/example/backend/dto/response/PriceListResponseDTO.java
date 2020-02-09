package com.example.backend.dto.response;

import com.example.backend.model.PriceList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListResponseDTO {

    private Integer priceListId;

    private Integer clinicId;

    private Integer typeOfExaminationId;

    private String typeOfExaminationName;

    private double price;

    private Integer priceListExaminations;

    public PriceListResponseDTO(PriceList priceList) {
        this.priceListId = priceList.getPriceListId();
        this.clinicId = priceList.getClinic().getClinicId();
        this.typeOfExaminationId = priceList.getTypeOfExamination().getTypeOfExaminationId();
        this.typeOfExaminationName = priceList.getTypeOfExamination().getName();
        this.price = priceList.getPrice();
        this.priceListExaminations = priceList.getExaminations()
                .stream()
                .filter(e -> !e.isDeleted() && !e.isFinished())
                .collect(Collectors.toList())
                .size();
    }
}
