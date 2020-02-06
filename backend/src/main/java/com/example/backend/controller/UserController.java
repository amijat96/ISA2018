package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.DoctorFreeTermsRequestDTO;
import com.example.backend.dto.request.UserRequestDTO;
import com.example.backend.dto.response.DoctorFreeTermsResponseDTO;
import com.example.backend.dto.response.ExaminationResponseDTO;
import com.example.backend.dto.response.MedicalRecordResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.service.UserService;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserResponseDTO> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new UserResponseDTO(userService.findByUsername(authentication.getName())));
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(new UserResponseDTO(userService.findByUsername(username)));
    }
    @GetMapping(path = "/free-terms")
    public ResponseEntity<List<DoctorFreeTermsResponseDTO>> getDoctorFreeTerms(@Valid @RequestBody DoctorFreeTermsRequestDTO doctorFreeTermsRequestDTO) {
        return ResponseEntity.ok(userService.getDoctorFreeTerms(doctorFreeTermsRequestDTO));
    }

    @GetMapping(path = "/{id}/medical-staff")
    public ResponseEntity<List<UserResponseDTO>> getClinicMedicalStaff(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getClinicMedicalStaff(id));
    }

    @GetMapping(path = "/{username}/medical-record")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecord(@PathVariable String username) {
        return ResponseEntity.ok(new MedicalRecordResponseDTO(userService.getMedicalRecord(username)));
    }

    @GetMapping(path = "/{username}/examinations")
    public ResponseEntity<List<ExaminationResponseDTO>> getExaminations(@PathVariable String username) {
        return ResponseEntity.ok(userService.getExaminations(username)
                .stream()
                .map(ExaminationResponseDTO::new)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(path = "doctor/{username}/examinations/{dateString}")
    public ResponseEntity<List<ExaminationResponseDTO>> getDoctorExaminationsByDate(@PathVariable String username, @PathVariable String dateString) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate date = formatter.parseLocalDate(dateString);
        return ResponseEntity.ok(userService.getDoctorExaminationsByDate(username, date)
                .stream()
                .map(ExaminationResponseDTO::new)
                .collect(Collectors.toList())
        );
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(new UserResponseDTO(userService.updateUser(userRequestDTO)));
    }

    @PutMapping(path = "/change-password/{password}")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.changePassword(authentication.getName(), password);
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully.", new ArrayList<>()));
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
        if(userService.deleteUser(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete user with id : " + id, new ArrayList<>()));
        }
    }
}
