package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "typeofexamination")
@NamedQuery(name = "TypeOfExamination.findAll", query = "SELECT toe FROM TypeOfExamination toe")
public class TypeOfExamination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_TYPE_OF_EXAMINATION")
    private int typeOfExaminationId;

    @Column(name = "DURATION")
    private LocalTime duration;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;

    @ManyToMany(mappedBy = "doctorSpecialization")
    private List<User> doctors;

    @OneToMany(mappedBy = "typeOfExamination")
    private List<PriceList> priceLists;

    @ManyToOne
    @JoinColumn(name = "ID_TYPE")
    private RoomType type;
}
