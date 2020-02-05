package com.example.backend.dto.response;

import com.example.backend.model.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private Integer reportId;

    private Integer nurseId;

    private String description;

    private boolean certified;

    private List<DiagnosisResponseDTO> diagnoses = new ArrayList<>();

    private List<MedicineResponseDTO> medicines = new ArrayList<>();

    public ReportResponseDTO(Report report) {
        this.reportId = report.getReportId();
        if(report.getNurse() != null)
            this.nurseId = report.getNurse().getUserId();
        this.description = report.getDescription();
        this.certified = report.isCertified();
        if(report.getDiagnoses() != null){
            this.diagnoses.addAll(report.getDiagnoses()
                            .stream()
                            .map(DiagnosisResponseDTO::new)
                            .collect(Collectors.toList())
                    );
        }
        if(report.getMedicines() != null){
            this.medicines.addAll(report.getMedicines()
                            .stream()
                            .map(MedicineResponseDTO::new)
                            .collect(Collectors.toList())
                    );
        }
    }
}
