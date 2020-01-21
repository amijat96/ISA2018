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
@Table(name = "report")
@NamedQuery(name = "Report.findAll", query = "SELECT r FROM Report r")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_REPORT")
    private int reportId;

    @OneToOne
    @JoinColumn(name = "ID_EXAMINATION")
    private Examination examination;

    @ManyToOne()
    @JoinColumn(name = "ID_USER")
    private User nurse;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MEDICINE_CERTIFIED")
    private boolean certified;

    @Column(name = "DELETED")
    private boolean deleted;

    @ManyToMany(mappedBy = "reports")
    private List<Diagnosis> diagnoses;

    @ManyToMany(mappedBy = "reports")
    private List<Medicine> medicines;

}
