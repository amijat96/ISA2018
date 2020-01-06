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
@Table(name = "diagnosis")
@NamedQuery(name = "Diagnosis.findAll", query = "SELECT d FROM Diagnosis d")
public class Diagnosis implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIAGNOSIS")
    private Diagnosis diagnosis;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;

    @ManyToMany
    @JoinTable(
            name="reportdiagnosis"
            , joinColumns={
            @JoinColumn(name="ID_REPORT")
        }
            , inverseJoinColumns={
            @JoinColumn(name="ID_DIAGNOSIS")
        }
    )
    private List<Report> reports;
}
