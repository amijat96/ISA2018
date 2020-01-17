package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.request.RoomFreeTermsRequestDTO;
import com.example.backend.dto.request.RoomRequestDTO;
import com.example.backend.dto.response.RoomFreeTermsResponseDTO;
import com.example.backend.dto.response.RoomResponseDTO;
import com.example.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByClinic(@RequestParam Integer id) {
        return ResponseEntity.ok(roomService
                .getRoomByClinicId(id)
                .stream()
                .map(RoomResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<RoomResponseDTO> getRoom(@PathVariable Integer id) {
        return ResponseEntity.ok(new RoomResponseDTO(roomService.getRoomById(id)));
    }

    @GetMapping(path = "/free-terms")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<List<RoomFreeTermsResponseDTO>> getFreeTerms(@Valid @RequestBody RoomFreeTermsRequestDTO roomFreeTermsRequestDTO) {
        return ResponseEntity.ok(roomService.getClinicFreeTerms(roomFreeTermsRequestDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO roomRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoomResponseDTO(roomService.createRoom(roomRequestDTO)));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Integer id, @Valid @RequestBody RoomRequestDTO roomRequestDTO) {
        return ResponseEntity.ok(new RoomResponseDTO(roomService.updateRoom(id, roomRequestDTO)));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN_CLINIC')")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable Integer id) {
        if(roomService.deleteRoom(id)) {
            return ResponseEntity.ok(new ApiResponse(true, "Room deleted successfully.", new ArrayList<>()));
        }
        else {
            return ResponseEntity.ok(new ApiResponse(false, "Could not delete room with given id.", new ArrayList<>()));
        }
    }

}
