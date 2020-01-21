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

    private String lastname;

    private String phone;

    private int gender;

    private String roleName;

    private String jbo;

    private String cityName;

    private String street;

    private String clinic;

    private LocalDate dateOfBirth;

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastname = user.getLastName();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.roleName = user.getRole().getName();
        this.cityName = user.getCity().getName();
        this.street = user.getStreet();
        this.jbo = user.getJbo();
        this.dateOfBirth = user.getDateOfBirth();
        if(user.getClinic() != null)
            this.clinic = user.getClinic().getName();
    }
}
