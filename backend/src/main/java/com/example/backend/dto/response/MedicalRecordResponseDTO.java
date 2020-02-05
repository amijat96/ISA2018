package com.example.backend.dto.response;

import com.example.backend.model.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponseDTO {

    private Integer medicalRecordId;

    private String bloodTypeRh;

    private double height;

    private double weight;

    private String race;

    public MedicalRecordResponseDTO(MedicalRecord medicalRecord){
        medicalRecordId = medicalRecord.getMedicalRecordId();
        bloodTypeRh = medicalRecord.getBloodTypeRh();
        height = medicalRecord.getHeight();
        weight = medicalRecord.getWeight();
        race = medicalRecord.getRace();
    }
}
