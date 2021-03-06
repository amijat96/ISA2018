package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinic")
@NamedQuery(name="Clinic.findAll", query="SELECT c FROM Clinic c")
public class Clinic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_CLINIC")
    private int clinicId;

    @Version
    private long version;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @ManyToOne()
    @JoinColumn(name="ID_CITY")
    private City city;

    @NotNull
    @Column(name = "STREET")
    private String street;

    @NotNull
    @Column(name =  "WORKTIME_START")
    private LocalTime workTimeStart;

    @NotNull
    @Column(name = "WORKTIME_END")
    private LocalTime workTimeEnd;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;

    @OneToMany(mappedBy = "clinic")
    private List<User> users;

    @OneToMany(mappedBy = "clinic")
    private List<Room> rooms;

    @OneToMany(mappedBy = "clinic")
    private List<PriceList> priceLists;

}
