package com.example.backend.controller;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.dto.response.VacationResponseDTO;
import com.example.backend.service.EmailService;
import com.example.backend.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_NURSE')")
    @Transactional
    public ResponseEntity<VacationResponseDTO> createVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        return ResponseEntity.ok(new VacationResponseDTO(vacationService.createVacation(vacationRequestDTO)));
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

    @GetMapping(path = "/{username}")
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_NURSE')")
    public ResponseEntity<List<VacationResponseDTO>> getDoctorVacations(@PathVariable String username){
        return ResponseEntity.ok(vacationService.getDoctorVacations(username)
                .stream()
                .map(VacationResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @Transactional
    @PutMapping(path="/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<VacationResponseDTO> approveVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        emailService.sendVacationApprovedMailToMedicalStaff(vacationRequestDTO);
        return ResponseEntity.ok(new VacationResponseDTO(vacationService.approveVacation(vacationRequestDTO.getVacationId())));
    }

    @Transactional
    @PutMapping(path="/deny")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<VacationResponseDTO> denyVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        emailService.sendVacationDeniedMailToMedicalStaff(vacationRequestDTO);
        return ResponseEntity.ok(new VacationResponseDTO(this.vacationService.denyVacation(vacationRequestDTO.getVacationId(), vacationRequestDTO.getDescription())));
    }
}
