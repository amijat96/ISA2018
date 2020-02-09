package com.example.backend.dto.response;

import com.example.backend.model.TypeOfExamination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfExaminationResponseDTO {

    private Integer id;

    private String name;

    private LocalTime duration;

    private String description;

    private Integer roomTypeId;

    private String typeName;

    private Integer numberOfDoctorsOrPriceLists;

    private boolean checked = false;

    public TypeOfExaminationResponseDTO(TypeOfExamination typeOfExamination) {
        this.id = typeOfExamination.getTypeOfExaminationId();
        this.name = typeOfExamination.getName();
        this.description = typeOfExamination.getDescription();
        this.duration = typeOfExamination.getDuration();
        this.roomTypeId = typeOfExamination.getType().getRoomTypeId();
        this.typeName = typeOfExamination.getType().getName();
        this.numberOfDoctorsOrPriceLists = typeOfExamination.getPriceLists()
                .stream()
                .filter(p -> !p.isDeleted())
                .collect(Collectors.toList())
                .size()
                +
                typeOfExamination.getDoctors()
                        .stream()
                        .filter(d -> !d.isDeleted())
                        .collect(Collectors.toList())
                        .size();
    }

}
