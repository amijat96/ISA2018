package com.example.backend.service;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.model.Vacation;

import java.util.List;

public interface VacationService {

    List<Vacation> getVacations(VacationsRequestDTO vacationsRequestDTO);

    Vacation crateVacation(VacationRequestDTO vacationRequestDTO);

    Vacation updateVacation(Integer id, VacationRequestDTO vacationRequestDTO);

    boolean deleteVacation(Integer id);

    Vacation approveVacation(Integer id);

    Vacation denyVacation(Integer id, String description);
}
