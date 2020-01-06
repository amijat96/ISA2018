package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule")
@NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM SCHEDULE s")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_SCHEDULE")
    private int scheduleId;

    @ManyToOne()
    @JoinColumn(name = "ID_USER")
    private User user;

    @Column(name = "SHIFT_SCHEDULE")
    private int shiftSchedule;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE_SCHEDULE")
    private Date startDateSchedule;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_SCHEDULE")
    private Date endDateSchedule;

    @Column(name = "DELETED")
    private boolean deleted;

}
