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
@NamedQuery(name="Clinic.findAll", query="SELECT c FROM Clinic c")
public class Clinic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_CLINIC")
    private int clinicId;
    /*
    private String name;

    @ManyToOne()
    @JoinColumn(name="ID_CITY")
    private int cityId;

    private String street;

    private Time worktimeStart;

    private Time worktimeEnd;

    private String description;

    private boolean deleted;*/
}
