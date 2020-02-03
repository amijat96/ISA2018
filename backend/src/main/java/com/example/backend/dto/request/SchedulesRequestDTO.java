package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulesRequestDTO {

    private Integer clinicId;

    private LocalDate startDate;

    private LocalDate endDate;
}
