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
@Table(name = "medicine")
@NamedQuery(name = "Medicine.findAll", query = "SELECT m FROM Medicine m")
public class Medicine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_MEDICINE")
    private int medicineId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;

    @ManyToMany
    @JoinTable(
            name="reportmedicine"
            , joinColumns={
            @JoinColumn(name="ID_REPORT")
        }
            , inverseJoinColumns={
            @JoinColumn(name="ID_MEDICINE")
        }
    )
    private List<Report> reports;
}
