package com.example.backend.controller;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.dto.response.ExaminationResponseDTO;
import com.example.backend.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/examination")
public class ExaminationController {

    private final ExaminationService examinationService;

    @Autowired
    public ExaminationController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }
    @PostMapping
    public ResponseEntity<ExaminationResponseDTO> createExamination(@Valid @RequestBody ExaminationRequestDTO examinatioRequestDTO)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new ExaminationResponseDTO(examinationService.
                                                            createExamination(authentication.getName(), examinatioRequestDTO)));
    }
}
