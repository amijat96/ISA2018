package com.example.backend.dto.response;

import com.example.backend.model.Medicine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineResponseDTO {

    private Integer medicineId;

    private String name;

    private String descripition;

    public MedicineResponseDTO(Medicine medicine) {
        this.medicineId = medicine.getMedicineId();
        this.name = medicine.getName();
        this.descripition = medicine.getDescription();
    }
}
