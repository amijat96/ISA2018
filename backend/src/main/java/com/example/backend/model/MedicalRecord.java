package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_record")
@NamedQuery(name = "MedicalRecord.findAll", query = "SELECT mr FROM MedicalRecord mr")
public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAL_RECORD")
    private int medicalRecordId;

    @Column(name = "BLOOD_TYPE_RH")
    private String bloodTypeRh;

    @Column(name = "HEIGHT")
    private double height;

    @Column(name = "WEIGHT")
    private double weight;

    @Column(name = "RACE")
    private String race;

    @Column(name = "DELETED")
    private boolean deleted;

}
