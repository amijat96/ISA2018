package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vacation")
@NamedQuery(name = "Vacation.findAll", query = "SELECT v FROM Vacation v")
public class Vacation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_VACATION")
    private int vacationId;

    @ManyToOne()
    @JoinColumn(name = "ID_USER")
    private User user;

    @Column(name = "ACCEPTED")
    private boolean accepted;

    @Column(name = "DURATION")
    private DateTime duration;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private boolean deleted;
}
