package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationRequestDTO {

    private Integer userId;

    @NotNull
    private Integer roomId;

    @NotNull
    private Integer typeId;

    @NotNull
    private Integer priceListId;

    private double discount;

    @NotNull
    private List<Integer> medicalStaffIds;
}
