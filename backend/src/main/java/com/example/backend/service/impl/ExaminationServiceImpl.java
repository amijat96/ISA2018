package com.example.backend.service.impl;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.ExaminationService;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationRepository examinationRepository;

    private final UserRepository userRepository;

    private final PriceListRepository priceListRepository;

    private final RoomRepository roomRepository;

    private final RoomTypeRepository roomTypeRepository;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public ExaminationServiceImpl(ExaminationRepository examinationRepository, UserRepository userRepository,
                                  PriceListRepository priceListRepository, RoomRepository roomRepository,
                                  RoomTypeRepository roomTypeRepository, JwtTokenProvider tokenProvider) {
        this.examinationRepository = examinationRepository;
        this.userRepository = userRepository;
        this.priceListRepository = priceListRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.tokenProvider = tokenProvider;
    }
    @Override
    public Examination createExamination(String username, ExaminationRequestDTO examinationRequestDTO) {
        Examination examination = new Examination();
        if(examinationRequestDTO.getUserId() != 0)
            examination.setUser(userRepository.findById(examinationRequestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Could not find user with given id.")));
        if(examinationRequestDTO.getRoomId() != 0)
            examination.setRoom(roomRepository.findById(examinationRequestDTO.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id")));
        examination.setRoomType(roomTypeRepository.findById(examinationRequestDTO.getTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find type with given id.")));
        User doctor = userRepository.findById(examinationRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could not find user with given id."));
        PriceList priceList = priceListRepository.findById(1)
                .orElseThrow(() -> new PriceListNotFoundException("Could not find price list with given id."));

        isDoctorWork(doctor, examinationRequestDTO);
        isDoctorFree(doctor, examinationRequestDTO, priceList);
        examination.setDoctor(doctor);
        examination.setPriceList(priceList);
        examination.setDateTime(examinationRequestDTO.getDateTime());

        Integer loggedUserRoleId = userRepository.findByUsername(username).getRole().getRoleId();
        //if logged user is clinic admin
        if(loggedUserRoleId == 2)
        {
            examination.setPredefined(true);
            examination.setDiscount(examinationRequestDTO.getDiscount());
        }
        examinationRepository.save(examination);
        return examination;
    }

    @Override
    public Examination approveExamination(Integer id, ExaminationRequestDTO examinationRequestDTO) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find examination with given id."));
        examination.setRoom(roomRepository.findById(examinationRequestDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id.")));
        User doctor = userRepository.findById(examinationRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could not find user with given id."));
        examination.setDoctor(doctor);
        examination.setDateTime(examinationRequestDTO.getDateTime());
        examinationRepository.save(examination);
        return examination;
    }

    public void isDoctorWork(User user, ExaminationRequestDTO examinationRequestDTO) {
        // check doctors schedule
        Schedule schedule = user.getSchedules()
                .stream()
                .filter(s -> s.getStartDateSchedule().isBefore(examinationRequestDTO.getDateTime().toLocalDate()) &&
                        s.getEndDateSchedule().isAfter(examinationRequestDTO.getDateTime().toLocalDate()))
                .findFirst()
                .orElseThrow(() -> new DoctorNotWorkException("Doctor with given id doesn't work that day."));

        //Start and end time of shift of day when user want's examination
        /*DateTime startDateTime = UserServiceImpl.createDateTime(examinationRequestDTO.getDateTime().toLocalDate(), schedule.getShiftStartTime());
        DateTime endDateTime = UserServiceImpl.createDateTime(examinationRequestDTO.getDateTime().toLocalDate(), schedule.getShiftEndTime());

        if(examinationRequestDTO.getDateTime().getMillis() <= startDateTime.getMillis() || examinationRequestDTO.getDateTime().getMillis() > endDateTime.getMillis()){
            throw new DoctorNotWorkException("Doctor with given id doesn't work at that time.");
        }*/

    }

    public void isDoctorFree(User user, ExaminationRequestDTO examinationRequestDTO, PriceList priceList){
        //check if term is free
        List<Examination> oldExaminations = user.getDoctorExaminations()
                .stream()
                .filter(e -> e.getDateTime().toLocalDate().compareTo(examinationRequestDTO.getDateTime().toLocalDate()) == 0)
                .sorted(Comparator.comparing(Examination::getDateTime))
                .collect(Collectors.toList());
        
        DateTime newExaminationEndTime = examinationRequestDTO.getDateTime().plus(priceList.getTypeOfExamination().getDuration().getMillisOfDay());

        for(Examination oldExamination: oldExaminations) {
            DateTime oldExaminationEndTime = oldExamination.getEndTime();
            long difference = examinationRequestDTO.getDateTime().getMillis() - oldExamination.getDateTime().getMillis();
            if(difference > 0) {
                if (examinationRequestDTO.getDateTime().getMillis() <= oldExaminationEndTime.getMillis()) {
                    throw new DoctorNotFreeException("Doctor with given id is not free at required time.");
                }
            }
            else if (difference < 0) {
                if(newExaminationEndTime.getMillis() >= oldExamination.getDateTime().getMillis() ) {
                    throw new DoctorNotFreeException("Doctor with given id is not free at required time.");
                }
            }
            else {
                throw new DoctorNotFreeException("Doctor with given id is not free at required time.");
            }
        }
    }

    @Override
    public Integer confirmExamination(String token){
        Integer examinationId = tokenProvider.getIdFromJwt(token);
        Examination examination = examinationRepository.findById(examinationId)
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find examination with given id."));

        DateTime dateTime = examination.getDateTime();
        DateTime now = new DateTime();

        if(Hours.hoursBetween(dateTime, now).getHours() >= 24) {
            examination.setAccepted(true);
            examinationRepository.save(examination);
        }
        else {
            throw new ExaminationModificationTimeExpiredException("Can't modify examination now.");
        }
        return examinationId;
    }

    @Override
    public Examination getExamination(Integer id) {
        return examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find examination with given id"));
    }

    ;
}
