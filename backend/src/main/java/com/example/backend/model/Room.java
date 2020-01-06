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
@Table(name = "room")
@NamedQuery(name = "Room.findAll", query = "SELECT r FROM ROOM r")
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_ROOM")
    private int roomId;

    @ManyToOne()
    @JoinColumn(name = "ID_CLINIC")
    private Clinic clinic;

    @ManyToOne()
    @JoinColumn(name = "ID_ROOM_TYPE")
    private RoomType roomType;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "FLOOR")
    private int floor;

    @Column(name = "DELETED")
    private boolean deleted;

    @OneToMany(mappedBy = "room")
    private List<Examination> examinations;

}
