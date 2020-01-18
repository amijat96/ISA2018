package com.example.backend.dto.response;

import com.example.backend.model.PriceList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListResponseDTO {

    private Integer priceListId;

    private Integer clinicId;

    private Integer typeOfExaminationId;

    private double price;

    public PriceListResponseDTO(PriceList priceList) {
        this.priceListId = priceList.getPriceListId();
        this.clinicId = priceList.getClinic().getClinicId();
        this.typeOfExaminationId = priceList.getTypeOfExamination().getTypeOfExaminationId();
        this.price = priceList.getPrice();
    }
}
