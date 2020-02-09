package com.example.backend.service.impl;

import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.dto.request.VacationsRequestDTO;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.DoctorNotFreeException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.exception.VacationNotFoundException;
import com.example.backend.model.Clinic;
import com.example.backend.model.User;
import com.example.backend.model.Vacation;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VacationRepository;
import com.example.backend.service.VacationService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationServiceImpl implements VacationService {

    private VacationRepository vacationRepository;

    private ClinicRepository clinicRepository;

    private UserRepository userRepository;

    @Autowired
    public VacationServiceImpl(VacationRepository vacationRepository, ClinicRepository clinicRepository,
                               UserRepository userRepository) {
        this.vacationRepository = vacationRepository;
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
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
    public List<Vacation> getDoctorVacations(String username) {
        return userRepository.findByUsername(username)
                .getVacations()
                .stream()
                .filter(v -> !v.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Vacation createVacation(VacationRequestDTO vacationRequestDTO) {
        User user = userRepository.findById(vacationRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Could not find user with given id"));
        LocalDate startDate = vacationRequestDTO.getStartDate();
        LocalDate endDate = vacationRequestDTO.getEndDate();

        if(user.getSchedules()
            .stream()
            .filter(s -> !s.isDeleted())
            .filter(s ->  ( (s.getStartDateSchedule().isBefore(startDate) || s.getStartDateSchedule().isEqual(startDate))&&(s.getEndDateSchedule().isAfter(startDate) || s.getEndDateSchedule().isEqual(startDate)) ) ||
                            ( (s.getStartDateSchedule().isBefore(endDate) || s.getStartDateSchedule().isEqual(endDate)) && (s.getEndDateSchedule().isAfter(endDate) || s.getEndDateSchedule().isEqual(startDate)) ) )
            .collect(Collectors.toList())
            .size() > 0) {
            throw new DoctorNotFreeException("You work during that period.");
        }
        Vacation vacation = new Vacation();
        vacation.setUser(user);
        vacation.setDescription(vacationRequestDTO.getDescription());
        vacation.setStartDate(startDate);
        vacation.setEndDate(endDate);
        vacationRepository.save(vacation);
        return vacation;
    }

    @Override
    @Transactional
    public Vacation approveVacation(Integer id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new VacationNotFoundException("Could not find vacation with given id"));
        vacation.setAccepted(true);
        vacationRepository.save(vacation);
        return vacation;
    }

    @Override
    @Transactional
    public Vacation denyVacation(Integer id, String description) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new VacationNotFoundException("Could not find vacation with given id"));
        vacation.setAccepted(false);
        vacation.setDescription(description);
        vacationRepository.save(vacation);
        return vacation;
    }
}
