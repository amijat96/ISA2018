package com.example.backend.service.impl;

import com.example.backend.dto.request.ClinicRequestDTO;
import com.example.backend.dto.request.ClinicReportRequestDTO;
import com.example.backend.dto.response.ReportByFrequencyDTO;
import com.example.backend.dto.response.ClinicReportResponseDTO;
import com.example.backend.exception.ClinicNotFoundException;
import com.example.backend.exception.DeletionException;
import com.example.backend.exception.ExaminationNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.CityRepository;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.service.ClinicService;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    private final CityRepository cityRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository clinicRepository, CityRepository cityRepository) {
        this.clinicRepository = clinicRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<Clinic> getClinics() {
        return clinicRepository.findAll().stream().filter(c -> !c.isDeleted()).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Clinic getClinic(Integer id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Clinic createClinic(ClinicRequestDTO clinicRequestDTO) {
        Clinic clinic = new Clinic();
        clinic.setCity(cityRepository.findById(clinicRequestDTO.getCityId())
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find city with given id")));
        clinic.setName(clinicRequestDTO.getName());
        clinic.setDescription(clinicRequestDTO.getDescription());
        clinic.setStreet(clinicRequestDTO.getStreet());
        clinic.setWorkTimeStart(clinicRequestDTO.getWorkTimeStart());
        clinic.setWorkTimeEnd(clinicRequestDTO.getWorkTimeEnd());
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Clinic updateClinic(Integer id, ClinicRequestDTO clinicRequestDTO) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id."));
        clinic.setName(clinicRequestDTO.getName());
        clinic.setWorkTimeStart(clinicRequestDTO.getWorkTimeStart());
        clinic.setWorkTimeEnd(clinicRequestDTO.getWorkTimeEnd());
        clinic.setStreet(clinicRequestDTO.getStreet());
        clinic.setDescription(clinicRequestDTO.getDescription());
        City city = cityRepository.findById(clinicRequestDTO.getCityId())
                .orElseThrow(() -> new ExaminationNotFoundException("Could not fin dcity with given id."));
        clinic.setCity(city);
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteClinic(Integer id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id."));
        if (clinic.getUsers().stream().filter(u -> !u.isDeleted()).collect(Collectors.toList()).size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        if(clinic.getRooms().stream().filter(r -> !r.isDeleted()).collect(Collectors.toList()).size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        if(clinic.getPriceLists().stream().filter(pl -> !pl.isDeleted()).collect(Collectors.toList()).size() > 0) {
            throw new DeletionException("Could not delete clinic with given id.");
        }
        try {
            clinicRepository.delete(clinic);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public List<User> getClinicPatients(Integer id) {
        List<User> patients = new ArrayList<>();
        Clinic clinic = getClinic(id);
        List<Room> rooms = clinic.getRooms();
        for(Room room : rooms) {
            patients.addAll(room.getExaminations()
                    .stream()
                    .filter(e -> e.getUser() != null)
                    .map(e -> e.getUser())
                    .distinct()
                    .collect(Collectors.toList()));

        }
        return patients.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public double getClinicRevenues(Integer clinicId, LocalDate startDate, LocalDate endDate) {
       return getClinicExaminations(clinicId, startDate, endDate).stream().filter(e -> e.isFinished()).map(e -> e.getPriceList().getPrice() *(1.0 - e.getDiscount()/100.0)).collect(Collectors.summingDouble(Double::doubleValue));
    }

    @Override
    public List<Examination> getAllClinicExaminations(Integer clinicId) {
        Clinic clinic = getClinic(clinicId);
        List<Examination> examinations = new ArrayList<>();
        if(clinic.getUsers().size() != 0) {
            List<User> users = clinic.getUsers()
                    .stream()
                    .filter(u -> u.getRole().getRoleId() == 3)
                    .collect(Collectors.toList());
            for (User user : users) {
                examinations.addAll(user.getDoctorExaminations()
                        .stream()
                        .filter(e -> !e.isDeleted())
                        .collect(Collectors.toList()));
            }
        }
        return examinations;
    }


    public List<Examination> getClinicExaminations(Integer clinicId, LocalDate startDate, LocalDate endDate) {
        List<Room> rooms = getClinic(clinicId).getRooms();
        List<Examination> examinations = new ArrayList<>();
        for(Room room : rooms) {
            examinations.addAll(room.getExaminations()
                    .stream()
                    .filter(e -> (e.getDateTime().toLocalDate().isAfter(startDate) || e.getDateTime().toLocalDate().isEqual(startDate)) &&
                            (e.getDateTime().toLocalDate().isEqual(endDate) || e.getDateTime().toLocalDate().isBefore(endDate)))
                    .collect(Collectors.toList()));
        }
        return examinations;
    }


    public List<Examination> getClinicExaminationsFromDateToDate(Integer clinicId, LocalDate startDate, LocalDate endDate) {
        List<Room> rooms = getClinic(clinicId).getRooms();
        List<Examination> examinations = new ArrayList<>();
        for(Room room : rooms) {
            examinations.addAll(room.getExaminations()
                    .stream()
                    .filter(e -> (e.getDateTime().toLocalDate().isAfter(startDate) || e.getDateTime().toLocalDate().isEqual(startDate)) && (e.getDateTime().toLocalDate().isBefore(endDate)))
                    .collect(Collectors.toList()));
        }
        return examinations;
    }

    @Override
    public double getClinicGrade(Integer clinicId) {
        List<Examination> examinations = getAllClinicExaminations(clinicId);
        return examinations.stream().map(e -> e.getGradeClinic()).collect(Collectors.summingDouble(Double::doubleValue))
                /examinations.stream().filter(e -> e.getGradeClinic() != 0).collect(Collectors.toList()).size();
    }

    @Override
    public ClinicReportResponseDTO getReport(Integer id, ClinicReportRequestDTO clinicReportRequestDTO) {
        ClinicReportResponseDTO reportResponse = new ClinicReportResponseDTO();
        List<ReportByFrequencyDTO> reports = new ArrayList<>();

        //set revenues
        reportResponse.setRevenues(getClinicRevenues(id, clinicReportRequestDTO.getFromDate(), clinicReportRequestDTO.getToDate()));

        LocalDate startDate = clinicReportRequestDTO.getFromDate();
        LocalDate endDate = clinicReportRequestDTO.getToDate();

        //Daily report
        if(clinicReportRequestDTO.getFrequency() == 0) {
            while(startDate.isBefore(endDate)) {
                ReportByFrequencyDTO reportByFrequency = new ReportByFrequencyDTO();
                reportByFrequency.setNumberOfExaminations(getClinicExaminationsFromDateToDate(id, startDate, startDate.plusDays(1))
                        .stream()
                        .filter(e -> !e.isDeleted())
                        .collect(Collectors.toList())
                        .size());
                reportByFrequency.setPeriod(startDate.toString());
                startDate = startDate.plusDays(1);
                reports.add(reportByFrequency);
            }
        }
        //Weekly report
        else if(clinicReportRequestDTO.getFrequency() == 1) {
            boolean periodLesThanWeek = false;
            Period period = new Period(startDate, endDate);
            while(startDate.isBefore(endDate)) {
                ReportByFrequencyDTO reportByFrequency = new ReportByFrequencyDTO();

                if(periodLesThanWeek) {
                    reportByFrequency.setPeriod(startDate.toString() + " - " + endDate.toString());
                    reportByFrequency.setNumberOfExaminations(getClinicExaminationsFromDateToDate(id, startDate, endDate)
                            .stream()
                            .filter(e -> !e.isDeleted())
                            .collect(Collectors.toList())
                            .size());
                }
                 else {
                    reportByFrequency.setPeriod(startDate.toString() + " - " + startDate.plusWeeks(1).toString());
                    reportByFrequency.setNumberOfExaminations(getClinicExaminationsFromDateToDate(id, startDate, startDate.plusWeeks(1))
                            .stream()
                            .filter(e -> !e.isDeleted())
                            .collect(Collectors.toList())
                            .size());
                }
                startDate = startDate.plusWeeks(1);
                reports.add(reportByFrequency);
            }
        }
        //Monthly report
        else if(clinicReportRequestDTO.getFrequency() == 2) {

            boolean firstReport = true;
            if(startDate.getDayOfMonth() == 1) firstReport = false;

            boolean periodLesThanMonth = false;
            if(endDate.getYear() == startDate.getYear() && endDate.getMonthOfYear() <= startDate.getMonthOfYear()) periodLesThanMonth = true;

            while(startDate.isBefore(endDate)) {
                ReportByFrequencyDTO reportByFrequency = new ReportByFrequencyDTO();

                if(periodLesThanMonth){
                    reportByFrequency.setPeriod(startDate.getDayOfMonth() +".-" + endDate.getDayOfMonth() + ". " + endDate.toString("MMM"));
                    reportByFrequency.setNumberOfExaminations(getClinicExaminationsFromDateToDate(id, startDate, endDate)
                            .stream()
                            .filter(e -> !e.isDeleted())
                            .collect(Collectors.toList())
                            .size());
                }
                else {
                    reportByFrequency.setNumberOfExaminations(getClinicExaminationsFromDateToDate(id, startDate,  new LocalDate(startDate.plusMonths(1).getYear(), startDate.plusMonths(1).getMonthOfYear(), 1 ))
                            .stream()
                            .filter(e -> !e.isDeleted())
                            .collect(Collectors.toList())
                            .size());
                    //if start date isn't first day of some month show it on diagram
                    if (firstReport) {
                        reportByFrequency.setPeriod(startDate.getDayOfMonth() + ".-" + startDate.dayOfMonth().getMaximumValue() + ". " + startDate.toString("MMM"));
                        firstReport = false;
                    } else {
                        reportByFrequency.setPeriod(startDate.toString("MMM"));
                    }
                }
                startDate = new LocalDate(startDate.plusMonths(1).getYear(), startDate.plusMonths(1).getMonthOfYear(), 1 );
                if(!periodLesThanMonth){
                    //if end date isn't last day of month, show it on diagram
                    if (!startDate.isBefore(endDate) && endDate.getDayOfMonth() < endDate.dayOfMonth().getMaximumValue()) {
                        reportByFrequency.setPeriod("1" + ".-" + endDate.getDayOfMonth() + ". " + endDate.toString("MMM"));
                    }
                }
                reports.add(reportByFrequency);
            }
        }
        reportResponse.setReports(reports);
        return reportResponse;
    }
}

