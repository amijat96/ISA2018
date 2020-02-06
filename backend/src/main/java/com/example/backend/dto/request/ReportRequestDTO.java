package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDTO {

    private String description;

    private List<Integer> medicines;

    private List<Integer> diagnoses;

}
