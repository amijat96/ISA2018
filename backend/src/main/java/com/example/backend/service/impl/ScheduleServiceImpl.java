package com.example.backend.service.impl;

import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.dto.request.SchedulesRequestDTO;
import com.example.backend.exception.*;
import com.example.backend.model.Clinic;
import com.example.backend.model.Schedule;
import com.example.backend.model.User;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(schedule.getUser().getRole().getRoleId() != 3 && schedule.getUser().getRole().getRoleId() != 4) throw new UserNotFoundException("User with id " + schedule.getUser().getUserId() + " is not doctor or nurse.");

        //check work time of clinic
        if(user.getClinic().getWorkTimeStart().getMillisOfDay() > scheduleRequestDTO.getShiftStartTime().getMillisOfDay() ||
                user.getClinic().getWorkTimeEnd().getMillisOfDay() < scheduleRequestDTO.getShiftEndTime().getMillisOfDay() ) {
            throw new ClinicNotWorkException("Clinic's work time is from " + user.getClinic().getWorkTimeStart() + " to " + user.getClinic().getWorkTimeEnd() + ".");
        }

        //check user's vacations or vacation requests
        if(user.getVacations()
                .stream()
                .filter(v -> v.isAccepted() || v.getDescription() == null)
                .filter(v -> ( (v.getStartDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getStartDate()))
                            && (v.getStartDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getStartDate().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                            ( (v.getEndDate().isAfter(scheduleRequestDTO.getStartDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getStartDate()))
                            && (v.getEndDate().isBefore(scheduleRequestDTO.getEndDate()) || v.getEndDate().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                        (v.getStartDate().isBefore(scheduleRequestDTO.getStartDate()) && v.getEndDate().isAfter(scheduleRequestDTO.getEndDate())))
                .collect(Collectors.toList())
                .size() > 0 ) { throw new UserHaveVacationException("User have vacation or unresolved vacation request during that period"); }

        if(user.getSchedules()
            .stream()
                .filter(s -> !s.isDeleted())
                .filter(s -> ( (s.getStartDateSchedule().isAfter(scheduleRequestDTO.getStartDate()) || s.getStartDateSchedule().isEqual(scheduleRequestDTO.getStartDate()))
                        && (s.getStartDateSchedule().isBefore(scheduleRequestDTO.getEndDate()) || s.getStartDateSchedule().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                        ( (s.getEndDateSchedule().isAfter(scheduleRequestDTO.getStartDate()) || s.getEndDateSchedule().isEqual(scheduleRequestDTO.getStartDate()))
                                && (s.getEndDateSchedule().isBefore(scheduleRequestDTO.getEndDate()) || s.getEndDateSchedule().isEqual(scheduleRequestDTO.getEndDate())) ) ||
                        (s.getStartDateSchedule().isBefore(scheduleRequestDTO.getStartDate()) && s.getEndDateSchedule().isAfter(scheduleRequestDTO.getEndDate())))
                .collect(Collectors.toList()).size() > 0) {
            throw new DoctorNotFreeException("Doctor or nurse isn't free during that period");
        }

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
    @Transactional
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
