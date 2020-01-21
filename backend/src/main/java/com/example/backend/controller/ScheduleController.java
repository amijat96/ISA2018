package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.dto.response.ScheduleResponseDTO;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController (ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Integer id) {
        return ResponseEntity.ok(new ScheduleResponseDTO(scheduleService.getScheduleById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLINIC_ADMIN')")
    @Transactional
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody ScheduleRequestDTO scheduleRequestDTO) {
        return ResponseEntity.ok(new ScheduleResponseDTO(scheduleService.createSchedule(scheduleRequestDTO)));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_CLINIC_ADMIN')")
    @Transactional
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Integer id, @Valid @RequestBody ScheduleRequestDTO scheduleRequestDTO) {
        return ResponseEntity.ok(new ScheduleResponseDTO(scheduleService.updateSchedule(id, scheduleRequestDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_CLINIC_ADMIN')")
    public ResponseEntity<ApiResponse> deleteSchedule(@PathVariable Integer id) {
        if(scheduleService.deleteSchedule(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "Room deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete room with given id.", new ArrayList<>()));
        }
    }
}
