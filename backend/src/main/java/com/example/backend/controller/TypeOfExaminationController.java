package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.TypeOfExaminationRequestDTO;
import com.example.backend.dto.response.TypeOfExaminationResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.service.TypeOfExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/types-of-examination")
public class TypeOfExaminationController {

    private TypeOfExaminationService typeOfExaminationService;

    @Autowired
    public TypeOfExaminationController(TypeOfExaminationService typeOfExaminationService) {
        this.typeOfExaminationService = typeOfExaminationService;
    }

    @GetMapping
    public ResponseEntity<List<TypeOfExaminationResponseDTO>> getTypesOfExamination() {
        return ResponseEntity.ok(typeOfExaminationService.getTypesOfExamination()
                .stream()
                .map(TypeOfExaminationResponseDTO::new)
                .collect(Collectors.toList())
        );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TypeOfExaminationResponseDTO> getTypeOfExamination(@PathVariable Integer id) {
        return ResponseEntity.ok(new TypeOfExaminationResponseDTO(typeOfExaminationService.getTypeOfExamination(id)));
    }

    @GetMapping(path = "/specialization/{id}")
    public ResponseEntity<List<UserResponseDTO>> getDoctorsBySpecialization(@PathVariable Integer id) {
        return ResponseEntity.ok(typeOfExaminationService.getDoctorBySpecialization(id)
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<TypeOfExaminationResponseDTO> createTypeOfExamination(@Valid @RequestBody TypeOfExaminationRequestDTO typeOfExaminationRequestDTO) {
        return ResponseEntity.ok(new TypeOfExaminationResponseDTO(typeOfExaminationService.createTypeOfExamination(typeOfExaminationRequestDTO)));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<TypeOfExaminationResponseDTO> updateTypeOfExamination(@PathVariable Integer id, @Valid @RequestBody TypeOfExaminationRequestDTO typeOfExaminationRequestDTO) {
        return ResponseEntity.ok(new TypeOfExaminationResponseDTO(typeOfExaminationService.updateTypeOfExamination(id, typeOfExaminationRequestDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<ApiResponse> deleteTypeOfExamination(@PathVariable Integer id) {
        if(typeOfExaminationService.deleteTypeOfExamination(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "Type of examination deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete type of examination with id : " + id, new ArrayList<>()));
        }
    }
}
