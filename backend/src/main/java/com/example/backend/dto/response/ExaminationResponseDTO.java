package com.example.backend.dto.response;

import com.example.backend.model.Examination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResponseDTO {

    private Integer examinationId;

    private Integer userId;

    private String userUsername;

    private Integer roomId;

    private String roomNumber;

    private Integer roomFloor;

    private String typeName;

    private Integer priceListId;

    private double price;

    private boolean predefined;

    private boolean finished;

    private double discount;

    private double gradeClinic;

    private double gradeDoctor;

    private boolean accepted;

    private DateTime dateTime;

    private Integer doctor;

    private String doctorUsername;

    public ExaminationResponseDTO(Examination examination) {
        this.examinationId = examination.getExaminationId();
        //user
        this.userId = examination.getUser().getUserId();
        this.userUsername = examination.getUser().getUsername();
        //room
        if(examination.getRoom() != null) {
            this.roomId = examination.getRoom().getRoomId();
            this.roomFloor = examination.getRoom().getFloor();
            this.roomNumber = examination.getRoom().getNumber();
        }
        //type
        this.typeName = examination.getPriceList().getTypeOfExamination().getName();

        //priceList
        this.priceListId = examination.getPriceList().getPriceListId();
        this.price = examination.getPriceList().getPrice() * (1.0 - examination.getDiscount()/100.0);
        this.discount = examination.getDiscount();

        //doctor
        this.doctor = examination.getDoctor().getUserId();
        this.doctorUsername = examination.getDoctor().getUsername();

        //grades
        this.gradeClinic = examination.getGradeClinic();
        this.gradeDoctor = examination.getGradeDoctor();

        //other
        this.predefined = examination.isPredefined();
        this.finished = examination.isFinished();
        this.accepted = examination.isAccepted();
        this.dateTime = examination.getDateTime();
    }
}
