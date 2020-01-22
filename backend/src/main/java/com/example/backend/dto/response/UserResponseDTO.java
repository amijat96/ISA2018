package com.example.backend.dto.response;

import com.example.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private int userId;

    private String username;

    private String email;

    private String name;

    private String lastName;

    private String phone;

    private int gender;

    private Integer roleId;

    private String jbo;

    private Integer cityId;

    private String street;

    private Integer clinicId;

    private LocalDate dateOfBirth;

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.roleId = user.getRole().getRoleId();
        this.cityId = user.getCity().getCityId();
        this.street = user.getStreet();
        this.jbo = user.getJbo();
        this.dateOfBirth = user.getDateOfBirth();
        if(user.getClinic() != null)
            this.clinicId = user.getClinic().getClinicId();
    }
}
