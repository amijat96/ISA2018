package com.example.backend.service.impl;

import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.dto.request.SchedulesRequestDTO;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.ScheduleNotFoundException;
import com.example.backend.exception.UserHaveVacationException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.Clinic;
import com.example.backend.model.Schedule;
import com.example.backend.model.User;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    private final ClinicRepository clinicRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository, ClinicRepository clinicRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public Schedule getScheduleById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Could not find schedule with id : " + id));
    }

    @Override
    @Transactional
    public Schedule createSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        Schedule schedule = new Schedule();
        User user = userRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could nod find doctor with id : " + scheduleRequestDTO.getDoctorId()));
        schedule.setUser(user);
        if(schedule.getUser().getRole().getRoleId() != 3 && schedule.getUser().getRole().getRoleId() != 4) throw new UserNotFoundException("User with id=" + schedule.getUser().getUserId() + " is not doctor or nurse.");

        if(user.getVacations()
                .stream()
                .filter(v -> ( (v.getStartDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getStartDate()))
                            && (v.getStartDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                            ( (v.getEndDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getStartDate()))
                            && (v.getEndDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getEndDate())) ) )
                .collect(Collectors.toList())
                .size() > 0 ) { throw new UserHaveVacationException("User have vacation in that period"); }

        schedule.setStartDateSchedule(scheduleRequestDTO.getStartDate());
        schedule.setEndDateSchedule(scheduleRequestDTO.getEndDate());
        schedule.setShiftStartTime(scheduleRequestDTO.getShiftStartTime());
        schedule.setShiftEndTime(scheduleRequestDTO.getShiftEndTime());
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Integer id, ScheduleRequestDTO scheduleRequestDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Could not find schedule with id : " + id));
        User user = userRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could nod find doctor with id : " + scheduleRequestDTO.getDoctorId()));
        schedule.setUser(user);
        if(schedule.getUser().getRole().getRoleId() != 3 && schedule.getUser().getRole().getRoleId() != 4) throw new UserNotFoundException("User with id=" + schedule.getUser().getUserId() + " is not doctor or nurse.");

        if(user.getVacations()
                .stream()
                .filter(v -> ( (v.getStartDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getStartDate()))
                        && (v.getStartDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                        ( (v.getEndDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getStartDate()))
                                && (v.getEndDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getEndDate())) ) )
                .collect(Collectors.toList())
                .size() > 0 ) { throw new UserHaveVacationException("User have vacation in that period"); }
        schedule.setStartDateSchedule(scheduleRequestDTO.getStartDate());
        schedule.setEndDateSchedule(scheduleRequestDTO.getEndDate());
        schedule.setShiftStartTime(scheduleRequestDTO.getShiftStartTime());
        schedule.setShiftEndTime(scheduleRequestDTO.getShiftEndTime());
        scheduleRepository.save(schedule);
        return schedule;
    }

    @Override
    public boolean deleteSchedule(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException("Could not find schedule with id : " + id));
        schedule.setDeleted(true);
        scheduleRepository.save(schedule);
        return true;
    }

    @Override
    public List<Schedule> getSchedules(SchedulesRequestDTO schedulesRequestDTO) {
        Clinic clinic = clinicRepository.findById(schedulesRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id"));
        List<User> medicalStaff = clinic.getUsers()
                .stream()
                .filter(u -> (u.getRole().getRoleId() == 3 || u.getRole().getRoleId() == 4) && !u.isDeleted())
                .collect(Collectors.toList());
        List<Schedule> schedules = new ArrayList<>();
        for (User user: medicalStaff) {
            schedules.addAll(user.getSchedules()
                    .stream()
                    .filter(s -> !s.isDeleted() && (( (schedulesRequestDTO.getStartDate().isBefore(s.getStartDateSchedule()) || schedulesRequestDTO.getStartDate().isEqual(s.getStartDateSchedule()))
                            && (schedulesRequestDTO.getEndDate().isAfter(s.getStartDateSchedule()) || schedulesRequestDTO.getEndDate().isEqual(s.getStartDateSchedule())) ) ||
                                ( (schedulesRequestDTO.getStartDate().isBefore(s.getEndDateSchedule()) || schedulesRequestDTO.getStartDate().isEqual(s.getEndDateSchedule()))
                            && (schedulesRequestDTO.getEndDate().isAfter(s.getEndDateSchedule()) || schedulesRequestDTO.getEndDate().isEqual(s.getEndDateSchedule())) )) ||
                            (s.getStartDateSchedule().isBefore(schedulesRequestDTO.getStartDate()) && s.getEndDateSchedule().isAfter(schedulesRequestDTO.getEndDate())))
                    .collect(Collectors.toList()));
        }
        return schedules;
    }
}
