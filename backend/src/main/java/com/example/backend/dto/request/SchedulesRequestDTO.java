package com.example.backend.dto.request;

import com.example.backend.miscellaneous.MyDateFormat;
import com.example.backend.miscellaneous.MyJsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesRequestDTO {

    private Integer clinicId;

    @MyDateFormat
    @JsonSerialize(using = MyJsonDateSerializer.class)
    private LocalDate startDate;

    @MyDateFormat
    @JsonSerialize(using = MyJsonDateSerializer.class)
    private LocalDate endDate;
}
