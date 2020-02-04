package com.example.backend.controller;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.dto.response.VacationResponseDTO;
import com.example.backend.service.EmailService;
import com.example.backend.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacations")
public class VacationController {

    private VacationService vacationService;

    private EmailService emailService;

    @Autowired
    public VacationController(VacationService vacationService, EmailService emailService) {
        this.vacationService = vacationService;
        this.emailService = emailService;
    }

    @PutMapping(path = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<List<VacationResponseDTO>> getVacations(@Valid @RequestBody VacationsRequestDTO vacationsRequestDTO) {
        return ResponseEntity.ok(vacationService.getVacations(vacationsRequestDTO)
                .stream()
                .map(VacationResponseDTO::new)
                .collect(Collectors.toList())
        );
    }

    @PutMapping(path="/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<VacationResponseDTO> approveVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        emailService.sendVacationApprovedMailToMedicalStaff(vacationRequestDTO);
        return ResponseEntity.ok(new VacationResponseDTO(vacationService.approveVacation(vacationRequestDTO.getVacationId())));
    }

    @PutMapping(path="/deny")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<VacationResponseDTO> denyVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        emailService.sendVacationDeniedMailToMedicalStaff(vacationRequestDTO);
        return ResponseEntity.ok(new VacationResponseDTO(this.vacationService.denyVacation(vacationRequestDTO.getVacationId(), vacationRequestDTO.getDescription())));
    }
}
