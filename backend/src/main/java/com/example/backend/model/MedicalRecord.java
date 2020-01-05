package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "MedicalRecord.findAll", query = "SELECT mr FROM MedicalRecord mr")
public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_MEDICAL_RECORD")
    private int medicalRecordId;
}
