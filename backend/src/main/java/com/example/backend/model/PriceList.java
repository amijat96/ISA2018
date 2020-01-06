package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pricelist")
@NamedQuery(name = "PriceList.findAll", query = "SELECT pl FROM PRICELIST pl")
public class PriceList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_PRICELIST")
    private int medicineId;

    @ManyToOne()
    @JoinColumn(name = "ID_CLINIC")
    private Clinic clinic;

    @ManyToOne()
    @JoinColumn(name = "ID_TYPE_OF_EXAMINATION")
    private TypeOfExamination typeOfExamination;

    @OneToMany(mappedBy = "pricelist")
    private List<Examination> examinations;
}
