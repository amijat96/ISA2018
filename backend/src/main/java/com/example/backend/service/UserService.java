package com.example.backend.service;

import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.DoctorFreeTermsRequestDTO;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.request.UserRequestDTO;
import com.example.backend.dto.response.DoctorFreeTermsResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.Examination;
import com.example.backend.model.MedicalRecord;
import com.example.backend.model.User;
import org.joda.time.LocalDate;

import java.util.List;

public interface UserService {

    JwtAuthDto login(LoginRequestDTO loginRequestDto);

    User register(RegisterRequestDTO registerRequestDto);

    Boolean confirmMail(String confirmToken);

    User findByUsername(String username);

    Boolean approveRegistration(int id);

    List<DoctorFreeTermsResponseDTO> getDoctorFreeTerms(DoctorFreeTermsRequestDTO doctorFreeTermsRequestDTO);

    List<UserResponseDTO> getClinicMedicalStaff(Integer clinicId);

    boolean deleteUser(Integer id);

    User updateUser(UserRequestDTO userRequestDTO);

    void changePassword(String username, String password);

    MedicalRecord getMedicalRecord(String username);

    List<Examination> getExaminations(String username);

    List<Examination> getDoctorExaminationsByDate(String username, LocalDate date);
}
