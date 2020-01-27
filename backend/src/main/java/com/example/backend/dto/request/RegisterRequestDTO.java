package com.example.backend.dto.request;

import com.example.backend.miscellaneous.MyDateFormat;
import com.example.backend.miscellaneous.MyJsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    @Size(min = 2, max = 40)
    private String name;

    @NotBlank
    @Size(min = 2, max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    private int gender;

    @Size(min = 6, max = 20)
    private String phone;

    @NotNull
    private int cityId;

    @NotBlank
    @Size(min = 6, max = 128)
    private String street;

    @Size(min = 13, max = 13)
    private String jbo;

    @MyDateFormat
    @JsonSerialize(using = MyJsonDateSerializer.class)
    private LocalDate dateOfBirth;

    @NotNull
    private Integer roleId;
}

