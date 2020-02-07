package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicReportResponseDTO {

    private double revenues;

    private List<ReportByFrequencyDTO> reports;

}
