package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule")
@NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM Schedule s")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ID_SCHEDULE")
    private int scheduleId;

    @ManyToOne()
    @JoinColumn(name = "ID_USER")
    private User user;

    @Column(name = "START_DATE_SCHEDULE")
    private LocalDate startDateSchedule;

    @Column(name = "END_DATE_SCHEDULE")
    private LocalDate endDateSchedule;

    @Column(name = "SHIFT_START_TIME")
    private LocalTime shiftStartTime;

    @Column(name = "SHIFT_END_TIME")
    private LocalTime shiftEndTime;

    @Column(name = "DELETED")
    private boolean deleted;

}
