package com.example.backend.service;

import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.DoctorFreeTermsRequestDTO;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.DoctorFreeTermsResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.User;

import java.util.List;

public interface UserService {

    JwtAuthDto login(LoginRequestDTO loginRequestDto);

    User register(RegisterRequestDTO registerRequestDto);

    Boolean confirmMail(String confirmToken);

    User findByUsername(String username);

    Boolean approveRegistration(int id);

    List<DoctorFreeTermsResponseDTO> getDoctorFreeTerms(DoctorFreeTermsRequestDTO doctorFreeTermsRequestDTO);

    List<UserResponseDTO> getClinicMedicalStaff(Integer clinicId);

}
