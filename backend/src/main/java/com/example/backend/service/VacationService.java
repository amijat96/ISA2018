package com.example.backend.service;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.model.Vacation;

import java.util.List;

public interface VacationService {

    List<Vacation> getVacations(VacationsRequestDTO vacationsRequestDTO);

    List<Vacation> getDoctorVacations(String username);

    Vacation createVacation(VacationRequestDTO vacationRequestDTO);

    Vacation approveVacation(Integer id);

    Vacation denyVacation(Integer id, String description);
}
