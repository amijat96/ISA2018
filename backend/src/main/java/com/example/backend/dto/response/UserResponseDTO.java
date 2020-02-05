package com.example.backend.dto.response;

import com.example.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private int id;

    private String username;

    private String email;

    private String name;

    private String lastName;

    private String phone;

    private int gender;

    private Integer roleId;

    private String jbo;

    private Integer cityId;

    private Integer countryId;

    private String street;

    private Integer clinicId;

    private LocalDate dateOfBirth;

    private double doctorGrade;

    private Integer numberOfSchedules;

    private boolean passwordChanged;

    private List<Integer> specializations = new ArrayList<>();

    public UserResponseDTO(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.roleId = user.getRole().getRoleId();
        this.cityId = user.getCity().getCityId();
        this.countryId = user.getCity().getCountry().getCountryId();
        this.street = user.getStreet();
        this.jbo = user.getJbo();
        this.dateOfBirth = user.getDateOfBirth();
        this.passwordChanged = user.isPasswordChanged();
        if(user.getClinic() != null)
            this.clinicId = user.getClinic().getClinicId();
        LocalDate date = LocalDate.now();
        this.numberOfSchedules = user.getSchedules()
                .stream()
                .filter(s -> s.getStartDateSchedule().isAfter(date) || s.getStartDateSchedule().isEqual(date) || s.getEndDateSchedule().isAfter(date) || s.getEndDateSchedule().isEqual(date))
                .collect(Collectors.toList())
                .size();
        this.specializations.addAll(user.getDoctorSpecialization()
                .stream()
                .map(t -> t.getTypeOfExaminationId())
                .collect(Collectors.toList())
        );
    }
}
