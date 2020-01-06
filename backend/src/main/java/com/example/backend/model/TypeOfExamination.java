package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "typeofexamination")
public class TypeOfExamination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_OF_EXAMINATION")
    private int typeOfExaminationId;

    @Temporal(TemporalType.TIME)
    @Column(name = "DURATION")
    private DateTime duration;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;

    @ManyToMany(mappedBy = "typeofexamination")
    private List<User> doctors;

    @OneToMany(mappedBy = "typeofexamination")
    private List<PriceList> priceLists;
}
