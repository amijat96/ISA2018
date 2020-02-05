package com.example.backend.dto.response;

import com.example.backend.model.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisResponseDTO {

    private Integer diagnosisId;

    private String name;

    private String description;

    public DiagnosisResponseDTO(Diagnosis diagnosis) {
        this.diagnosisId = diagnosis.getDiagnosisId();
        this.name = diagnosis.getName();
        this.description = diagnosis.getDescription();
    }
}
