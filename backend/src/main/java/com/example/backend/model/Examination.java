package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "examination")
@NamedQuery(name = "Examination.findAll", query = "SELECT e FROM Examination e")
public class Examination implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EXAMINATION")
    private int examinationId;

    @ManyToOne()
    @JoinColumn(name = "ID_USER")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "ID_ROOM")
    private Room room;

    @ManyToOne()
    @JoinColumn(name = "ID_ROOM_TYPE")
    private RoomType roomType;

    @ManyToOne()
    @JoinColumn(name = "ID_PRICELIST")
    private PriceList priceList;

    @Column(name = "DISCOUNT")
    private double discount;

    @Column(name = "PREDEFINED")
    private boolean predefined;

    @Column(name = "FINISHED")
    private boolean finished;

    @Column(name = "GRADE_CLINIC")
    private double gradeClinic;

    @Column(name = "GRADE_DOCTOR")
    private double gradeDoctor;

    @Column(name = "ACCEPTED")
    private boolean accepted;

    @Column(name = "DELETED")
    private boolean deleted;

    @Column(name = "CANCELED")
    private boolean canceled;

    @Column(name = "DATE_TIME")
    private DateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "ID_DOCTOR")
    private User doctor;

    public DateTime getEndTime() {
        return dateTime.plus(this.priceList.getTypeOfExamination().getDuration().getMillisOfDay());
    }
}