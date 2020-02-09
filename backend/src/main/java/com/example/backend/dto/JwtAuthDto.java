package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthDto {

    private final String tokenType = "Bearer";

    private String accessToken;

    private String role;

    private Integer clinicId;

    private String clinicName;
}
