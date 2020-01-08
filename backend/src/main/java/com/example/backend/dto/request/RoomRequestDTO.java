package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDTO {

    @NotNull
    private String number;

    @NotNull
    private Integer floor;

    @NotNull
    private Integer clinicId;

    @NotNull
    private Integer roomTypeId;

}
