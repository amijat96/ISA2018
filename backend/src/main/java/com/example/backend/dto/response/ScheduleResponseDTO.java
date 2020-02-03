package com.example.backend.dto.response;

import com.example.backend.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {

    private Integer scheduleId;

    private Integer doctorId;

    private String doctorUsername;

    private String doctorName;

    private String doctorLastName;

    private String role;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime shiftStartTime;

    private LocalTime shiftEndTime;

    public ScheduleResponseDTO(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.doctorId = schedule.getUser().getUserId();
        this.doctorUsername = schedule.getUser().getUsername();
        this.doctorName = schedule.getUser().getName();
        this.doctorLastName = schedule.getUser().getLastName();
        this.role = schedule.getUser().getRole().getName();
        this.startDate = schedule.getStartDateSchedule();
        this.endDate = schedule.getEndDateSchedule();
        this.shiftStartTime = schedule.getShiftStartTime();
        this.shiftEndTime = schedule.getShiftEndTime();
    }
}
