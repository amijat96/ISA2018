package com.example.backend.service.impl;

import com.example.backend.dto.request.RoomFreeTermsRequestDTO;
import com.example.backend.dto.request.RoomRequestDTO;
import com.example.backend.dto.response.RoomFreeTermsResponseDTO;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.RoomRepository;
import com.example.backend.repository.RoomTypeRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.RoomService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final RoomTypeRepository roomTypeRepository;

    private final ClinicRepository clinicRepository;

    private final UserRepository userRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository,
                           ClinicRepository clinicRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Room> getRoomByClinic(String username) {
        Clinic clinic = userRepository.findByUsername(username).getClinic();
        return clinic.getRooms().stream()
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
        room.setExaminations(new ArrayList<>());
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
    @Transactional
    public List<RoomFreeTermsResponseDTO> getClinicFreeTerms(RoomFreeTermsRequestDTO roomFreeTermsRequestDTO) {

        List<RoomFreeTermsResponseDTO> freeTerms = new ArrayList<>();
        LocalDate dateFromRequest = roomFreeTermsRequestDTO.getDateTime().toLocalDate();
        Clinic clinic = clinicRepository.findById(roomFreeTermsRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id."));

        //Find all rooms in the clinic, with same type as examination
        List<Room> rooms = clinic.getRooms()
                .stream()
                .filter(r -> r.getRoomType().getRoomTypeId() == roomFreeTermsRequestDTO.getRoomTypeId() && !r.isDeleted())
                .collect(Collectors.toList());

        //Find free terms for every room
        for(Room room : rooms) {

            //Find doctors schedule
            User doctor  = userRepository.findById(roomFreeTermsRequestDTO.getDoctorId())
                    .orElseThrow(() -> new DoctorNotFreeException("Could not find doctor with given id."));

            //Find all free terms on date from request, or first free term on next date
            boolean termFound = false;
            LocalDate date = dateFromRequest;

            while(!(termFound && !date.isEqual(dateFromRequest))) {
                LocalDate dateIterator = date;

                //Doctors schedule for examination date
                Schedule schedule = doctor.getSchedules()
                        .stream()
                        .filter(s -> (s.getStartDateSchedule().isBefore(dateIterator) || s.getStartDateSchedule().isEqual(dateIterator)))
                        .filter(s -> (s.getEndDateSchedule().isAfter(dateIterator) || s.getEndDateSchedule().isEqual(dateIterator)))
                        .findFirst()
                        .orElseThrow(() -> new DoctorNotWorkException("Doctor doesn't work."));

                //All examinations in room on requested examination date
                List<Examination> examinations = room.getExaminations()
                        .stream()
                        .filter(e -> e.getDateTime().toLocalDate().isEqual(dateIterator))
                        .collect(Collectors.toList());
                examinations.sort(Comparator.comparing(Examination::getDateTime));

                //Shift start and end time. Find free terms between them. Similar as in method UserServiceImpl.getDoctorFreeTerms().
                DateTime startTime = UserServiceImpl.createDateTime(dateIterator, schedule.getShiftStartTime());
                DateTime endTime = UserServiceImpl.createDateTime(dateIterator, schedule.getShiftEndTime().minusMinutes(1));
                if(!examinations.isEmpty()) {
                    for (Examination examination : examinations) {
                        if (examination.getDateTime().getMillis() - startTime.getMillis() >= roomFreeTermsRequestDTO.getDuration().getMillisOfDay() &&
                                endTime.getMillis() - examination.getEndTime().getMillis() >= roomFreeTermsRequestDTO.getDuration().getMillisOfDay()) {
                            termFound = true;
                            freeTerms.add(new RoomFreeTermsResponseDTO(room.getRoomId(), startTime, examination.getDateTime().minusMinutes(1).minus(roomFreeTermsRequestDTO.getDuration().getMillisOfDay())));
                        }
                        startTime = examination.getEndTime().plusMinutes(1);
                    }
                    if (endTime.getMillis() - startTime.getMillis() >= roomFreeTermsRequestDTO.getDuration().getMillisOfDay()) {
                        termFound = true;
                        freeTerms.add(new RoomFreeTermsResponseDTO(room.getRoomId(), startTime, endTime.minus(roomFreeTermsRequestDTO.getDuration().getMillisOfDay())));
                    }
                }
                else {
                    termFound = true;
                    freeTerms.add(new RoomFreeTermsResponseDTO(room.getRoomId(), startTime, endTime.minus(roomFreeTermsRequestDTO.getDuration().getMillisOfDay())));
                }
                date = dateIterator.plusDays(1);
            }
        }
        return freeTerms;
    }

    @Override
    @Transactional
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
