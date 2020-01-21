package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorFreeTermsResponseDTO {

    private DateTime startDateTime;

    private DateTime endDateTime;

}
