package com.example.backend.service.impl;

import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.exception.ScheduleNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.Schedule;
import com.example.backend.repository.ScheduleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
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
        schedule.setUser(userRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could nod find doctor with id : " + scheduleRequestDTO.getDoctorId())));
        if(schedule.getUser().getRole().getRoleId() != 3 && schedule.getUser().getRole().getRoleId() != 4) throw new UserNotFoundException("User with id=" + schedule.getUser().getUserId() + " is not doctor or nurse.");
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
        schedule.setUser(userRepository.findById(scheduleRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("Could nod find doctor with id : " + scheduleRequestDTO.getDoctorId())));
        if(schedule.getUser().getRole().getRoleId() != 3 && schedule.getUser().getRole().getRoleId() != 4) throw new UserNotFoundException("User with id=" + schedule.getUser().getUserId() + " is not doctor or nurse.");
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
        return true;
    }
}
