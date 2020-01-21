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

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime shiftStartTime;

    private LocalTime shiftEndTime;

    public ScheduleResponseDTO(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.doctorId = schedule.getUser().getUserId();
        this.startDate = schedule.getStartDateSchedule();
        this.endDate = schedule.getEndDateSchedule();
        this.shiftStartTime = schedule.getShiftStartTime();
        this.shiftEndTime = schedule.getShiftEndTime();
    }
}
