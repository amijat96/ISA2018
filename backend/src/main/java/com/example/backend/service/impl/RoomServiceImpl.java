package com.example.backend.service.impl;

import com.example.backend.dto.request.RoomRequestDTO;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.RoomNotFoundException;
import com.example.backend.exception.RoomTypeNotFoundException;
import com.example.backend.model.Room;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.RoomRepository;
import com.example.backend.repository.RoomTypeRepository;
import com.example.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final RoomTypeRepository roomTypeRepository;

    private final ClinicRepository clinicRepository;
    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository,
                           ClinicRepository clinicRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.clinicRepository = clinicRepository;
    }
    @Override
    public List<Room> getRoomByClinicId(Integer id) {
        return roomRepository.findAll()
                .stream()
                .filter(r -> r.getClinic().getClinicId() == id)
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Room getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id."));
        return room;
    }

    @Override
    @Transactional
    public Room createRoom(RoomRequestDTO roomRequestDTO) {
        Room room = new Room();
        room.setClinic(clinicRepository.findById(roomRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id.")));
        room.setRoomType(roomTypeRepository.findById(roomRequestDTO.getRoomTypeId())
            .orElseThrow(() -> new RoomTypeNotFoundException("Could not find room type with given id.")));
        room.setNumber(roomRequestDTO.getNumber());
        room.setFloor(roomRequestDTO.getFloor());
        roomRepository.save(room);
        return room;
    }

    @Override
    @Transactional
    public Room updateRoom(Integer id, RoomRequestDTO roomRequestDTO) {
        Room room = getRoomById(id);
        room.setClinic(clinicRepository.findById(roomRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id.")));
        room.setRoomType(roomTypeRepository.findById(roomRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find room type with given id.")));
        room.setNumber(roomRequestDTO.getNumber());
        room.setFloor(roomRequestDTO.getFloor());
        roomRepository.save(room);
        return room;
    }

    @Override
    public boolean deleteRoom(Integer id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id."));
        if(room.isDeleted() || room.getExaminations()
                .stream()
                .filter(e -> !e.isFinished())
                .collect(Collectors.toList()).size() > 0) {
            return false;
        }
        else {
            room.setDeleted(true);
            roomRepository.save(room);
            return true;
        }
    }
}
