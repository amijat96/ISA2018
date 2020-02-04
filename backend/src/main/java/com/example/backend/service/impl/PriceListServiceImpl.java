package com.example.backend.service.impl;

import com.example.backend.dto.request.PriceListRequestDTO;
import com.example.backend.exception.*;
import com.example.backend.model.Clinic;
import com.example.backend.model.Examination;
import com.example.backend.model.PriceList;
import com.example.backend.model.TypeOfExamination;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.PriceListRepository;
import com.example.backend.repository.TypeOfExaminationRepository;
import com.example.backend.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceListServiceImpl implements PriceListService {

    private final PriceListRepository priceListRepository;

    private final ClinicRepository clinicRepository;

    private final TypeOfExaminationRepository typeOfExaminationRepository;
    @Autowired
    public PriceListServiceImpl(PriceListRepository priceListRepository, ClinicRepository clinicRepository,
                                TypeOfExaminationRepository typeOfExaminationRepository) {
        this.priceListRepository = priceListRepository;
        this.clinicRepository = clinicRepository;
        this.typeOfExaminationRepository = typeOfExaminationRepository;
    }

    @Override
    public PriceList getPriceListById(Integer priceListId) {
        return priceListRepository.findById(priceListId)
                .orElseThrow(() -> new PriceListNotFoundException("Could not find price list with id : " + priceListId));
    }

    @Override
    public List<PriceList> getPriceListByClinic(Integer clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with id : " + clinicId));
        return clinic.getPriceLists()
                .stream()
                .filter(pl -> !pl.isDeleted())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PriceList createPriceList(PriceListRequestDTO priceListRequestDTO) {
        Clinic clinic = clinicRepository.findById(priceListRequestDTO.getClinicId())
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with id : " + priceListRequestDTO.getClinicId()));
        //if item with given type of examination already exist in clinic, throw exception
        if (getPriceListByClinic(clinic.getClinicId())
                .stream()
                .filter(i -> i.getTypeOfExamination().getTypeOfExaminationId() == priceListRequestDTO.getTypeOfExaminationId())
                .filter(i -> !i.isDeleted())
                .findFirst()
                .isPresent()) {
            throw new PriceListAlreadyExistException("Item already exist in price list.");
        }
        TypeOfExamination typeOfExamination = typeOfExaminationRepository.findById(priceListRequestDTO.getTypeOfExaminationId())
                .orElseThrow(() -> new TypeOfExaminationNotFoundException("Could not find type of examination with id : " + priceListRequestDTO.getTypeOfExaminationId()));
        PriceList priceList = new PriceList();

        priceList.setClinic(clinic);
        priceList.setTypeOfExamination(typeOfExamination);
        priceList.setPrice(priceListRequestDTO.getPrice());
        priceList.setExaminations(new ArrayList<>());
        priceListRepository.save(priceList);
        return (priceList);
    }

    @Transactional
    @Override
    public PriceList updatePriceList(Integer priceListId, PriceListRequestDTO priceListRequestDTO) {
        PriceList priceList = getPriceListById(priceListId);
        List<Examination> examinations = priceList.getExaminations()
                .stream()
                .filter(e ->!e.isDeleted())
                .collect(Collectors.toList());

        if(examinations.size() == 0 && !priceList.isDeleted()) {
            Clinic clinic = clinicRepository.findById(priceListRequestDTO.getClinicId())
                    .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with id : " + priceListRequestDTO.getClinicId()));
            TypeOfExamination typeOfExamination = typeOfExaminationRepository.findById(priceListRequestDTO.getTypeOfExaminationId())
                    .orElseThrow(() -> new TypeOfExaminationNotFoundException("Could not find type of examination with id : " + priceListRequestDTO.getTypeOfExaminationId()));
            priceList.setClinic(clinic);
            priceList.setTypeOfExamination(typeOfExamination);
            priceList.setPrice(priceListRequestDTO.getPrice());
            priceListRepository.save(priceList);
            return (priceList);
        }
        else {
            throw new PriceListNotUpdatedException("Could not update price list with id : " + priceListId);
        }
    }

    @Override
    public boolean deletePriceList(Integer priceListId) {
        PriceList priceList = getPriceListById(priceListId);
        List<Examination> examinations = priceList.getExaminations()
                .stream()
                .filter(e -> !e.isFinished() && !e.isDeleted())
                .collect(Collectors.toList());
        if(examinations.size() == 0) {
            priceList.setDeleted(true);
            priceListRepository.save(priceList);
            return true;
        }
        else {
            return false;
        }
    }
}
