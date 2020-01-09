package com.example.backend.service.impl;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.exception.PriceListNotFoundException;
import com.example.backend.exception.RoomNotFoundException;
import com.example.backend.exception.RoomTypeNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.Examination;
import com.example.backend.model.User;
import com.example.backend.repository.*;
import com.example.backend.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationRepository examinationRepository;

    private final UserRepository userRepository;

    private final PriceListRepository priceListRepository;

    private final RoomRepository roomRepository;

    private final RoomTypeRepository roomTypeRepository;

    @Autowired
    public ExaminationServiceImpl(ExaminationRepository examinationRepository, UserRepository userRepository,
                                  PriceListRepository priceListRepository, RoomRepository roomRepository,
                                  RoomTypeRepository roomTypeRepository) {
        this.examinationRepository = examinationRepository;
        this.userRepository = userRepository;
        this.priceListRepository = priceListRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }
    @Override
    public Examination createExamination(String username, ExaminationRequestDTO examinationRequestDTO) {
        Examination examination = new Examination();
        if(examinationRequestDTO.getUserId() != 0)
            examination.setUser(userRepository.findById(examinationRequestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Could not find user with given id.")));
        examination.setRoom(roomRepository.findById(examinationRequestDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id.")));
        examination.setRoomType(roomTypeRepository.findById(examinationRequestDTO.getTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find type with given id.")));
        examination.setPriceList(priceListRepository.findById(examinationRequestDTO.getPriceListId())
                .orElseThrow(() -> new PriceListNotFoundException("Could not find price list with given id.")));
        
        // set medical staff for examination
        List<User> medicalStaff = new ArrayList<>();
        for (Integer userId: examinationRequestDTO.getMedicalStaffIds()) {
            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new UserNotFoundException("Could not find user with given id."));
            medicalStaff.add(user);
        }
        
        
        Integer loggedUserRoleId = userRepository.findByUsername(username).getRole().getRoleId();
        
        //if logged user is doctor 
        if(loggedUserRoleId == 3) {
            medicalStaff.add(userRepository.findByUsername(username));
        }
        //if logged user is clinic admin
        else if(loggedUserRoleId == 2)
        {
            examination.setPredefined(true);
            examination.setAccepted(true);
            examination.setDiscount(examinationRequestDTO.getDiscount());
        }
        examination.setMedicalStaff(medicalStaff
                                        .stream()
                                        .collect(Collectors.toList()));


        examinationRepository.save(examination);
        return examination;
    }

}
