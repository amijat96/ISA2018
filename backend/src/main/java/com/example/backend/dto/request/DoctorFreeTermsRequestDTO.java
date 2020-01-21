package com.example.backend.dto.request;

import com.example.backend.miscellaneous.MyDateTimeFormat;
import com.example.backend.miscellaneous.MyJsonDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorFreeTermsRequestDTO {

    private Integer doctorId;

    @MyDateTimeFormat
    @JsonSerialize(using = MyJsonDateTimeSerializer.class)
    DateTime dateTime;

    LocalTime duration;
}
