package com.example.backend.service;

import com.example.backend.dto.request.ScheduleRequestDTO;
import com.example.backend.model.Schedule;

public interface ScheduleService {

    Schedule getScheduleById(Integer id);

    Schedule createSchedule(ScheduleRequestDTO scheduleRequestDTO);

    Schedule updateSchedule(Integer id, ScheduleRequestDTO scheduleRequestDTO);

    boolean deleteSchedule(Integer id);

}
