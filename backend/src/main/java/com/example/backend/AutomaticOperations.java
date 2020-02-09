package com.example.backend;

import com.example.backend.dto.request.ExaminationRequestDTO;
import com.example.backend.dto.request.RoomFreeTermsRequestDTO;
import com.example.backend.dto.response.RoomFreeTermsResponseDTO;
import com.example.backend.model.Clinic;
import com.example.backend.model.Examination;
import com.example.backend.service.ClinicService;
import com.example.backend.service.EmailService;
import com.example.backend.service.ExaminationService;
import com.example.backend.service.RoomService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.StrictMath.abs;

@Component
public class AutomaticOperations {

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void approveExaminationsAutomatically() {
        DateTime dateTime = new DateTime();

        List<Clinic> clinics = clinicService.getClinics();
        for(Clinic clinic: clinics){
            List<Examination> examinations = clinicService.getAllClinicExaminations(clinic.getClinicId())
                    .stream()
                    .filter(e -> e.getDateTime().getMillis() - dateTime.getMillis() < 24 * 60 * 60 * 1000 && e.getRoom() == null)
                    .collect(Collectors.toList());
            for(Examination examination: examinations){
                approveExamination(examination);
            }
        }
    }

    public void approveExamination(Examination examination){
        //create free terms request
        RoomFreeTermsRequestDTO freeTerms = new RoomFreeTermsRequestDTO();
        freeTerms.setClinicId(examination.getDoctor().getClinic().getClinicId());
        freeTerms.setDoctorId(examination.getDoctor().getUserId());
        freeTerms.setRoomTypeId(examination.getRoomType().getRoomTypeId());
        freeTerms.setDateTime(examination.getDateTime());
        freeTerms.setDuration(examination.getPriceList().getTypeOfExamination().getDuration());

        long examinationDateTime = examination.getDateTime().getMillis();
        DateTime min = examination.getDateTime();
        Integer roomId = 0;

        //get free terms for all rooms
        List<RoomFreeTermsResponseDTO> roomFreeTerms = roomService.getClinicFreeTerms(freeTerms);

        //find best term
        for(RoomFreeTermsResponseDTO freeTerm: roomFreeTerms){

            long startDateTime = freeTerm.getStartDateTime().getMillis();
            long endDateTime = freeTerm.getEndDateTime().getMillis();

            //if requested time is between start & end time of free term , set examination date&time and break for loop
            if(examinationDateTime >= startDateTime && examinationDateTime <= endDateTime){
                roomId = freeTerm.getRoomId();
                min = examination.getDateTime();
                break;
            }
            //if it's not, find nearest date&time from free term
            else {
                if(abs(min.getMillis() - startDateTime) < abs(min.getMillis() - endDateTime)) {
                    min = freeTerm.getStartDateTime();
                    roomId = freeTerm.getRoomId();
                }
                else {
                    min = freeTerm.getEndDateTime();
                    roomId = freeTerm.getRoomId();
                }
            }


        }

        //create examination request
        ExaminationRequestDTO examinationRequestDTO = new ExaminationRequestDTO(
                examination.getUser().getUserId()
                ,roomId
                ,examination.getDoctor().getUserId()
                ,examination.getRoomType().getRoomTypeId()
                ,examination.getPriceList().getPriceListId()
                ,0
                ,min);

        //set room and date&time for examination
        Examination approvedExamination = examinationService.approveExamination(examination.getExaminationId(), examinationRequestDTO);
        emailService.sendConfirmationMailToPatient(approvedExamination);
        emailService.sendConfirmationMailToDoctor(approvedExamination.getExaminationId());
    }
}
