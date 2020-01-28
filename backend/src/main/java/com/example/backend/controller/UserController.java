package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.DoctorFreeTermsRequestDTO;
import com.example.backend.dto.response.DoctorFreeTermsResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/free-terms")
    public ResponseEntity<List<DoctorFreeTermsResponseDTO>> getDoctorFreeTerms(@Valid @RequestBody DoctorFreeTermsRequestDTO doctorFreeTermsRequestDTO) {
        return ResponseEntity.ok(userService.getDoctorFreeTerms(doctorFreeTermsRequestDTO));
    }

    @GetMapping(path = "/{id}/medical-staff")
    public ResponseEntity<List<UserResponseDTO>> getClinicMedicalStaff(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getClinicMedicalStaff(id));
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
