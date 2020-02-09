package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfExaminationRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private LocalTime duration;

    private String description;

    @NotNull
    private Integer roomTypeId;

}
