package com.example.backend.service.impl;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.VacationNotFoundException;
import com.example.backend.model.Clinic;
import com.example.backend.model.User;
import com.example.backend.model.Vacation;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.VacationRepository;
import com.example.backend.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationServiceImpl implements VacationService {

    private VacationRepository vacationRepository;

    private ClinicRepository clinicRepository;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository, ClinicRepository clinicRepository) {
        this.vacationRepository = vacationRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Vacation> getVacations(VacationsRequestDTO vacationsRequestDTO) {
        Clinic clinic = clinicRepository.findById(vacationsRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("COuld not find clinic with given id."));
        List<User> medicalStaff = clinic.getUsers()
                .stream()
                .filter(u -> !u.isDeleted() && (u.getRole().getRoleId() == 3 || u.getRole().getRoleId() == 4))
                .collect(Collectors.toList());

        List<Vacation> vacations = new ArrayList<>();
        for(User user : medicalStaff) {
            vacations.addAll(user.getVacations()
                    .stream()
                    .filter(v -> !v.isDeleted())
                    .filter(v -> ( ( ((vacationsRequestDTO.getStartDate().isBefore(v.getStartDate()) || vacationsRequestDTO.getStartDate().isEqual(v.getStartDate()))
                            && (vacationsRequestDTO.getEndDate().isAfter(v.getStartDate()) || vacationsRequestDTO.getEndDate().isEqual(v.getStartDate())) ) ||
                            ( (vacationsRequestDTO.getStartDate().isBefore(v.getEndDate()) || vacationsRequestDTO.getStartDate().isEqual(v.getEndDate()))
                                    && (vacationsRequestDTO.getEndDate().isAfter(v.getEndDate()) || vacationsRequestDTO.getEndDate().isEqual(v.getEndDate())) )) ||
                            (v.getStartDate().isBefore(vacationsRequestDTO.getStartDate()) && v.getEndDate().isAfter(vacationsRequestDTO.getEndDate()))))
                    .collect(Collectors.toList()));
        }
        return vacations;
    }

    @Override
    public Vacation crateVacation(VacationRequestDTO vacationRequestDTO) {
        return null;
    }

    @Override
    public Vacation updateVacation(Integer id, VacationRequestDTO vacationRequestDTO) {
        return null;
    }

    @Override
    public boolean deleteVacation(Integer id) {
        return false;
    }

    @Override
    public Vacation approveVacation(Integer id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new VacationNotFoundException("Could not find vacation with given id"));
        vacation.setAccepted(true);
        vacationRepository.save(vacation);
        return vacation;
    }

    @Override
    public Vacation denyVacation(Integer id, String description) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new VacationNotFoundException("Could not find vacation with given id"));
        vacation.setAccepted(false);
        vacation.setDescription(description);
        vacationRepository.save(vacation);
        return vacation;
    }
}
