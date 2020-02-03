package com.example.backend.service.impl;

import com.example.backend.dto.request.TypeOfExaminationRequestDTO;
import com.example.backend.exception.RoomTypeNotFoundException;
import com.example.backend.exception.TypeOfExaminationNotFoundException;
import com.example.backend.model.RoomType;
import com.example.backend.model.TypeOfExamination;
import com.example.backend.model.User;
import com.example.backend.repository.RoomTypeRepository;
import com.example.backend.repository.TypeOfExaminationRepository;
import com.example.backend.service.TypeOfExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeOfExaminationServiceImpl implements TypeOfExaminationService {

    TypeOfExaminationRepository typeOfExaminationRepository;

    RoomTypeRepository roomTypeRepository;

    @Autowired
    public TypeOfExaminationServiceImpl(TypeOfExaminationRepository typeOfExaminationRepository, RoomTypeRepository roomTypeRepository) {
        this.typeOfExaminationRepository = typeOfExaminationRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public List<TypeOfExamination> getTypesOfExamination() {
        return typeOfExaminationRepository.findAll()
                .stream()
                .filter(t -> !t.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public TypeOfExamination createTypeOfExamination(TypeOfExaminationRequestDTO typeOfExaminationRequestDTO) {
        TypeOfExamination typeOfExamination = new TypeOfExamination();
        typeOfExamination.setName(typeOfExaminationRequestDTO.getName());
        typeOfExamination.setDuration(typeOfExaminationRequestDTO.getDuration());
        typeOfExamination.setDescription(typeOfExaminationRequestDTO.getDescription());
        RoomType roomType = roomTypeRepository.findById(typeOfExaminationRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find room type with given id."));
        typeOfExamination.setType(roomType);
        typeOfExamination.setDoctors(new ArrayList<>());
        typeOfExamination.setPriceLists(new ArrayList<>());
        typeOfExaminationRepository.save(typeOfExamination);
        return typeOfExamination;
    }

    @Override
    public TypeOfExamination updateTypeOfExamination(Integer id, TypeOfExaminationRequestDTO typeOfExaminationRequestDTO) {
        TypeOfExamination typeOfExamination = typeOfExaminationRepository.findById(id)
                .orElseThrow(() -> new TypeOfExaminationNotFoundException("Could not find type of examination with given id."));
        typeOfExamination.setDuration(typeOfExaminationRequestDTO.getDuration());
        typeOfExamination.setDescription(typeOfExaminationRequestDTO.getDescription());
        RoomType roomType = roomTypeRepository.findById(typeOfExaminationRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new RoomTypeNotFoundException("Could not find room type with given id."));
        typeOfExamination.setType(roomType);
        typeOfExaminationRepository.save(typeOfExamination);
        return typeOfExamination;
    }

    @Override
    public boolean deleteTypeOfExamination(Integer id) {
        TypeOfExamination typeOfExamination = typeOfExaminationRepository.findById(id)
                .orElseThrow(() -> new TypeOfExaminationNotFoundException("Could not find type of examination with given id."));
        if(typeOfExamination.getPriceLists().size() == 0 || typeOfExamination.getDoctors().size() == 0) {
            typeOfExamination.setDeleted(true);
            typeOfExaminationRepository.save(typeOfExamination);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<User> getDoctorBySpecialization(Integer id) {
        TypeOfExamination typeOfExamination = typeOfExaminationRepository.findById(id)
                .orElseThrow(() -> new TypeOfExaminationNotFoundException("Could not find type of examination with given id."));
        return typeOfExamination.getDoctors()
                .stream()
                .filter(d -> !d.isDeleted())
                .collect(Collectors.toList());
    }
}
