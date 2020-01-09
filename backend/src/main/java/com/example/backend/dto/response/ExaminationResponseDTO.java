package com.example.backend.dto.response;

import com.example.backend.model.Examination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResponseDTO {

    private Integer examinationId;

    private Integer userId;

    private Integer roomId;

    private Integer typeId;

    private Integer priceListId;

    private boolean predefined;

    private boolean finished;

    private double discount;

    private double gradeClinic;

    private double gradeDoctor;

    private boolean accepted;

    private List<Integer> medicalStaffIds;

    public ExaminationResponseDTO(Examination examination) {
        this.examinationId = examination.getExaminationId();
        this.userId = examination.getUser().getUserId();
        this.roomId = examination.getRoom().getRoomId();
        this.typeId = examination.getRoomType().getRoomTypeId();
        this.priceListId = examination.getPriceList().getPriceListId();
        this.predefined = examination.isPredefined();
        this.finished = examination.isFinished();
        this.discount = examination.getDiscount();
        this.gradeClinic = examination.getGradeClinic();
        this.gradeDoctor = examination.getGradeDoctor();
        this.accepted = examination.isAccepted();
        this.medicalStaffIds = examination.getMedicalStaff()
                .stream()
                .map(m -> m.getUserId())
                .collect(Collectors.toList());
    }
}
