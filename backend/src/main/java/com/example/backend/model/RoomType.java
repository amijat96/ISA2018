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
@Table(name = "roomtype")
@NamedQuery(name = "RoomType.findAll", query = "SELECT rt FROM RoomType rt")
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_ROOM_TYPE")
    private int roomTypeId;

    @Version
    private long version;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;

    @OneToMany(mappedBy = "roomType")
    private List<Examination> examinations;

    @OneToMany(mappedBy = "type")
    private List<TypeOfExamination> typeOfExaminations;

}
