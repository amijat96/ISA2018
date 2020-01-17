package com.example.backend.dto.response;

import com.example.backend.model.Clinic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicResponseDTO {

    private int clinicId;

    private String name;

    private Integer cityId;

    private String street;

    private LocalTime workTimeStart;

    private LocalTime workTimeEnd;

    private String description;

    private List<Integer> priceListIds;

    private List<Integer> roomIds;

    private List<Integer> doctorIds;

    private List<Integer> nurseIds;

    public ClinicResponseDTO(Clinic clinic) {
        this.clinicId = clinic.getClinicId();
        this.name = clinic.getName();
        this.cityId = clinic.getCity().getCityId();
        this.street = clinic.getStreet();
        this.workTimeStart = clinic.getWorkTimeStart();
        this.workTimeEnd = clinic.getWorkTimeEnd();
        this.description = clinic.getDescription();
        if(clinic.getPriceLists() != null) {
            this.priceListIds = clinic.getPriceLists()
                    .stream()
                    .map(c -> c.getPriceListId())
                    .collect(Collectors.toList());
        }
        else {
            this.priceListIds = new ArrayList<>();
        }
        if(clinic.getRooms() != null) {
            this.roomIds = clinic.getRooms()
                    .stream()
                    .map(r -> r.getRoomId())
                    .collect(Collectors.toList());
        }
        else {
            this.roomIds = new ArrayList<>();
        }
        if(clinic.getUsers() != null) {
            this.doctorIds = clinic.getUsers()
                    .stream()
                    .filter(u -> u.getRole().getRoleId() == 3)
                    .map(u -> u.getUserId())
                    .collect(Collectors.toList());
            this.nurseIds = clinic.getUsers()
                    .stream()
                    .filter(u -> u.getRole().getRoleId() == 4)
                    .map(u -> u.getUserId())
                    .collect(Collectors.toList());
        }
        else {
            this.doctorIds = new ArrayList<>();
            this.nurseIds = new ArrayList<>();
        }

    }
}
