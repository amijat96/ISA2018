package com.example.backend.service;

import com.example.backend.dto.request.RoomRequestDTO;
import com.example.backend.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> getRoomByClinicId(Integer id);

    Room getRoomById(Integer id);

    Room createRoom(RoomRequestDTO roomRequestDTO);

    Room updateRoom(Integer id, RoomRequestDTO roomRequestDTO);

    boolean deleteRoom(Integer id);
}
