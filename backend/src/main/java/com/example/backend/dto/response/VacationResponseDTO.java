package com.example.backend.dto.response;

import com.example.backend.model.Vacation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationResponseDTO {
    private Integer vacationId;

    private Integer userId;

    private String userUsername;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private boolean accepted;

    public VacationResponseDTO(Vacation vacation) {
        vacationId = vacation.getVacationId();
        userId = vacation.getUser().getUserId();
        userUsername = vacation.getUser().getUsername();
        startDate = vacation.getStartDate();
        endDate = vacation.getEndDate();
        description = vacation.getDescription();
        accepted = vacation.isAccepted();
    }
}
