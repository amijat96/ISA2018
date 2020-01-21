package com.example.backend.service;

import com.example.backend.dto.request.RoomFreeTermsRequestDTO;
import com.example.backend.dto.request.RoomRequestDTO;
import com.example.backend.dto.response.RoomFreeTermsResponseDTO;
import com.example.backend.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> getRoomByClinic(String usernamed);

    Room getRoomById(Integer id);

    Room createRoom(RoomRequestDTO roomRequestDTO);

    Room updateRoom(Integer id, RoomRequestDTO roomRequestDTO);

    List<RoomFreeTermsResponseDTO> getClinicFreeTerms(RoomFreeTermsRequestDTO roomFreeTermsRequestDTO);

    boolean deleteRoom(Integer id);
}
