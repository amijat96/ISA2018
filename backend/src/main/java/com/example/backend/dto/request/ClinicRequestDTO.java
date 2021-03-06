package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicRequestDTO {

    @NotBlank
    @Size(min = 2, max = 40)
    private String name;

    @NotNull
    private Integer cityId;

    @NotBlank
    @Size(min = 6, max = 128)
    private String street;

    @NotNull
    private LocalTime workTimeStart;

    @NotNull
    private LocalTime workTimeEnd;

    @Size(max = 1024)
    private String description;


}
