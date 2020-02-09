package com.example.backend.controller;

import com.example.backend.dto.response.RoomTypeResponseDTO;
import com.example.backend.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/roomTypes")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeResponseDTO>> getRoomTypes() {
        return ResponseEntity.ok(roomTypeService.getAllRoomTypes()
                                                .stream()
                                                .map(RoomTypeResponseDTO::new)
                                                .collect(Collectors.toList()));
    }
}
