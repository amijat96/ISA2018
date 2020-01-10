package com.example.backend.dto.request;

import com.example.backend.miscellaneous.DateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;
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

    private List<Integer> medicalStaffIds;

    @NotNull
    @JsonDeserialize(using = DateHandler.class)
    private Date date;

    @NotNull
    private Time startTime;
}
