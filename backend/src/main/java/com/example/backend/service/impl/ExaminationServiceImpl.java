package com.example.backend.service.impl;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.exception.*;
import com.example.backend.model.Examination;
import com.example.backend.model.PriceList;
import com.example.backend.model.Schedule;
import com.example.backend.model.User;
import com.example.backend.repository.*;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        /*examination.setRoom(roomRepository.findById(examinationRequestDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id.")));*/
        examination.setRoomType(roomTypeRepository.findById(examinationRequestDTO.getTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find type with given id.")));
        PriceList priceList = priceListRepository.findById(examinationRequestDTO.getPriceListId())
                .orElseThrow(() -> new PriceListNotFoundException("Could not find price list with given id."));
        examination.setPriceList(priceList);
        
        // set medical staff to examination
        List<User> medicalStaff = new ArrayList<>();
        for (Integer userId: examinationRequestDTO.getMedicalStaffIds()) {
            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new UserNotFoundException("Could not find user with given id."));
            isDoctorWork(user, examinationRequestDTO);
            isDoctorFree(user, examinationRequestDTO, priceList);
            medicalStaff.add(user);
        }

        examination.setDate(examinationRequestDTO.getDate());
        examination.setStartTime(examinationRequestDTO.getStartTime());

        Integer loggedUserRoleId = userRepository.findByUsername(username).getRole().getRoleId();
        
        //if logged user is doctor 
        if(loggedUserRoleId == 3) {
            medicalStaff.add(userRepository.findByUsername(username));
        }
        //if logged user is clinic admin
        else if(loggedUserRoleId == 2)
        {
            examination.setPredefined(true);
            examination.setDiscount(examinationRequestDTO.getDiscount());
        }
        examination.setMedicalStaff(medicalStaff
                                        .stream()
                                        .collect(Collectors.toList()));

        examinationRepository.save(examination);
        return examination;
    }

    @Override
    public Examination approveExamination(Integer id, ExaminationRequestDTO examinationRequestDTO) {
        Examination examination = examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find examination with given id."));
        examination.setRoom(roomRepository.findById(examinationRequestDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Could not find room with given id.")));
        List<User> medicalStaff = new ArrayList<>();
        for (Integer userId: examinationRequestDTO.getMedicalStaffIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Could not find user with given id."));
            medicalStaff.add(user);
        }
        examination.setMedicalStaff(medicalStaff);
        examination.setStartTime(examinationRequestDTO.getStartTime());
        examinationRepository.save(examination);
        return examination;
    }

    public void isDoctorWork(User user, ExaminationRequestDTO examinationRequestDTO) {
        // check if doctor/nurse is work
        List <Schedule> schedules = user.getSchedules()
                .stream()
                .filter(s -> s.getStartDateSchedule().before(examinationRequestDTO.getDate()) &&
                        s.getEndDateSchedule().after(examinationRequestDTO.getDate()))
                .collect(Collectors.toList());
        if(schedules.size() > 0) {
            Schedule schedule = schedules.get(0);
            Integer shift = schedule.getShiftSchedule();
            if(examinationRequestDTO.getStartTime().getHours() >= 8 && examinationRequestDTO.getStartTime().getHours() < 16 && shift != 1){
                throw new DoctorNotWorkException("Doctor with given id doesn't work at that time. Shift 1");
            }
            else if(examinationRequestDTO.getStartTime().getHours() >= 16 && examinationRequestDTO.getStartTime().getHours() < 24 && shift != 2){
                throw new DoctorNotWorkException("Doctor with given id doesn't work at that time. Shift 2");
            }
            else if (examinationRequestDTO.getStartTime().getHours() >= 0 && examinationRequestDTO.getStartTime().getHours() < 8 && shift != 3){
                throw new DoctorNotWorkException("Doctor with given id doesn't work at that time.");
            }

        }
        else {
            throw new DoctorNotWorkException("Doctor with given id doesn't work that day.");
        }
    };

    public void isDoctorFree(User user, ExaminationRequestDTO examinationRequestDTO, PriceList priceList){
        //check if term is free
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        
        List<Examination> oldExaminations = user.getMedicalStaff()
                .stream()
                .filter(e -> e.getDate().compareTo(examinationRequestDTO.getDate()) == 0)
                .collect(Collectors.toList());
        
        Time newExaminationEndTime = new Time(0);
        newExaminationEndTime.setMinutes(examinationRequestDTO.getStartTime().getMinutes() + priceList.getTypeOfExamination().getDuration().getMinutes());
        newExaminationEndTime.setHours(examinationRequestDTO.getStartTime().getHours() + priceList.getTypeOfExamination().getDuration().getHours());

        for(Examination oldExamination: oldExaminations) {
            PriceList ePriceList = priceListRepository.findById(oldExamination.getPriceList().getPriceListId())
                    .orElseThrow(() -> new PriceListNotFoundException("Could not find price list with given id."));
            Time oldExaminationEndTime = new Time(0);
            oldExaminationEndTime.setMinutes(oldExamination.getStartTime().getMinutes() + ePriceList.getTypeOfExamination().getDuration().getMinutes());
            oldExaminationEndTime.setHours(oldExamination.getStartTime().getHours() + ePriceList.getTypeOfExamination().getDuration().getHours());
            Integer difference = examinationRequestDTO.getStartTime().compareTo(oldExamination.getStartTime());
            if(difference > 0) {
                if (examinationRequestDTO.getStartTime().before(oldExaminationEndTime) || examinationRequestDTO.getStartTime().equals(oldExaminationEndTime)) {
                    throw new DoctorNotFreeException("Doctor with given id is not free at required time.");
                }
            }
            else if (difference < 0) {
                if(!newExaminationEndTime.before(oldExamination.getStartTime()) || newExaminationEndTime.equals(oldExamination.getStartTime())) {
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
        java.util.Date now = new Date();

        /**
         * Change java.util.Date and java.sql.Time libraries(outdated)
         */
        Date date = examination.getDate();
        Time time = examination.getStartTime();
        // Construct date and time objects
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        // Extract the time of the "time" object to the "date"
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        // Get the time value!
        date = dateCal.getTime();

        long days = TimeUnit.DAYS.convert(Math.abs(now.getTime() - date.getTime()), TimeUnit.MILLISECONDS);
        if(days > 0) {
            examination.setAccepted(true);
            examinationRepository.save(examination);
        }
        else {
            throw new ExaminationModificationTimeExpiredException("Can't modify examination now.");
        }
        return examinationId;
    };
}
