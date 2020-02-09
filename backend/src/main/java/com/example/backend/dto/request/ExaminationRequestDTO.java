package com.example.backend.dto.request;

import com.example.backend.miscellaneous.MyDateTimeFormat;
import com.example.backend.miscellaneous.MyJsonDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationRequestDTO {

    private Integer userId;

    private Integer roomId;

    @NotNull
    private Integer doctorId;

    @NotNull
    private Integer typeId;

    @NotNull
    private Integer priceListId;

    private double discount;


    @MyDateTimeFormat
    @JsonSerialize(using = MyJsonDateTimeSerializer.class)
    private DateTime dateTime;

}
