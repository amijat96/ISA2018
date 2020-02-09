package com.example.backend.service;

import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.dto.request.SchedulesRequestDTO;
import com.example.backend.model.Schedule;

import java.util.List;

public interface ScheduleService {

    Schedule getScheduleById(Integer id);

    Schedule createSchedule(ScheduleRequestDTO scheduleRequestDTO);

    Schedule updateSchedule(Integer id, ScheduleRequestDTO scheduleRequestDTO);

    boolean deleteSchedule(Integer id);

    List<Schedule> getSchedules(SchedulesRequestDTO schedulesRequestDTO);
}
