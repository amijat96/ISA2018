package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.dto.response.ExaminationResponseDTO;
import com.example.backend.model.Examination;
import com.example.backend.service.EmailService;
import com.example.backend.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/examination")
public class ExaminationController {

    private final ExaminationService examinationService;

    private final EmailService emailService;

    @Autowired
    public ExaminationController(ExaminationService examinationService, EmailService emailService) {
        this.examinationService = examinationService;
        this.emailService = emailService;
    }
    @PostMapping
    @Transactional
    public ResponseEntity<ExaminationResponseDTO> createExamination(@Valid @RequestBody ExaminationRequestDTO examinatioRequestDTO) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new ExaminationResponseDTO(examinationService.
                                                            createExamination(authentication.getName(), examinatioRequestDTO)));
    }

    @PutMapping(path = "/approve-examination")
    @PreAuthorize("hasRole('ROLE_ADMIN_CLINIC')")
    public ResponseEntity<ExaminationResponseDTO> approveExamination(@PathParam("id") Integer id, @Valid @RequestBody ExaminationRequestDTO examinationRequestDTO) {
        Examination  examination = examinationService.approveExamination(id, examinationRequestDTO);
        emailService.sendConfirmationMailToPatient(examination);
        return ResponseEntity.ok(new ExaminationResponseDTO(examination));
    }

    @GetMapping(path = "/confirm-examination")
    public ResponseEntity<ApiResponse> confirmExamination(@PathParam("token") String token) {
        Integer examinationId = examinationService.confirmExamination(token);
        if(examinationId != 0) {
            emailService.sendConfirmationMailToDoctor(examinationId);
            return ResponseEntity.ok(new ApiResponse(true, "Examination confirmed successfully.", new ArrayList<>()));
        }
        else {
            return new ResponseEntity(new ApiResponse(false, "Examination confirmation failed.", new ArrayList<>()), HttpStatus.BAD_REQUEST);
        }
    }


}
