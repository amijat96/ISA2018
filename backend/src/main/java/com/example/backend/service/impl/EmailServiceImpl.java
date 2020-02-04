package com.example.backend.service.impl;

import com.example.backend.config.ConfigProperties;
import com.example.backend.dto.request.VacationRequestDTO;
import com.example.backend.exception.ExaminationNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.Examination;
import com.example.backend.model.User;
import com.example.backend.repository.ExaminationRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.EmailService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

@Component
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    private final ServletContext servletContext;

    private final JwtTokenProvider tokenProvider;

    private final SpringTemplateEngine templateEngine;

    private final ConfigProperties configProperties;

    private final UserRepository userRepository;

    private final ExaminationRepository examinationRepository;

    @Autowired
    public EmailServiceImpl(ConfigProperties configProperties, UserRepository userRepository,
                            JavaMailSender javaMailSender, ServletContext servletContext,
                            JwtTokenProvider tokenProvider, SpringTemplateEngine templateEngine,
                            ExaminationRepository examinationRepository) {
        this.javaMailSender = javaMailSender;
        this.servletContext = servletContext;
        this.configProperties = configProperties;
        this.tokenProvider = tokenProvider;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
        this.examinationRepository = examinationRepository;
    }

    @Async
    void sendMail(String email, String subject, Context context, String template) {

        final String body;
        body = templateEngine.process(template, context);

        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeHelper;
        try {
            mimeHelper = new MimeMessageHelper(message, true);
            mimeHelper.setTo(email);
            mimeHelper.setSubject(subject);
            mimeHelper.setText(body, true);
            javaMailSender.send(mimeHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendConfirmationMailToUser(Integer id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        logger.info(String.format("Sending confirmation token to %s email.", user.getEmail()));
        Context context = new Context();
        context.setVariable("title", configProperties.getConfirmSubject());
        context.setVariable("firstName", user.getName());
        context.setVariable("emailConfirmLink", configProperties.getFrontBaseUrl() + "/confirm-email?token=" + tokenProvider.generateConfirmationToken(user.getUserId()));
        sendMail(user.getEmail(), configProperties.getConfirmSubject(), context, "email_confirmation.html");
    }

    @Override
    public void sendConfirmationMailToPatient(Examination examination) {
        final User patient = userRepository.findByUsername(examination.getUser().getUsername());
            sendNotificationMail(patient, examination);
    }

    @Override
    public void sendConfirmationMailToDoctor(Integer examinationId) {
        Examination examination = examinationRepository.findById(examinationId)
                .orElseThrow(() -> new ExaminationNotFoundException("Could not find examination with given id."));
        sendNotificationMail(examination.getDoctor(), examination);

    }

    @Override
    public void sendVacationApprovedMailToMedicalStaff(VacationRequestDTO vacationRequestDTO) {
        final User user = userRepository.findById(vacationRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        logger.info(String.format("Sending vacation mail for to %s .", user.getEmail()));
        Context context = new Context();
        context.setVariable("type", "vacation request");
        context.setVariable("title", "Vacation request approved");
        context.setVariable("firstName", user.getName());
        context.setVariable("fromDate", vacationRequestDTO.getStartDate());
        context.setVariable("toDate", vacationRequestDTO.getEndDate());
        context.setVariable("clinicName", user.getClinic().getName());
        sendMail(user.getEmail(), "Vacation request approved", context, "email_approved_vacation.html");
    }

    @Override
    public void sendVacationDeniedMailToMedicalStaff(VacationRequestDTO vacationRequestDTO) {
        final User user = userRepository.findById(vacationRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        logger.info(String.format("Sending vacation mail for to %s .", user.getEmail()));
        Context context = new Context();
        context.setVariable("type", "vacation request");
        context.setVariable("title", "Vacation request denied");
        context.setVariable("firstName", user.getName());
        context.setVariable("fromDate", vacationRequestDTO.getStartDate());
        context.setVariable("toDate", vacationRequestDTO.getEndDate());
        context.setVariable("clinicName", user.getClinic().getName());
        context.setVariable("description", vacationRequestDTO.getDescription());
        sendMail(user.getEmail(), "Vacation request denied", context, "email_denied_vacation.html");
    }

    void sendNotificationMail(User user, Examination examination) {
        final User doctor = userRepository.findById(examination.getDoctor().getUserId())
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        logger.info(String.format("Sending confirmation mail for examination/operation to %s email.", user.getEmail()));
        Context context = new Context();
        context.setVariable("type", examination.getRoomType().getName().toLowerCase());
        context.setVariable("title", configProperties.getExaminationConfirmed());
        context.setVariable("firstName", user.getName());
        context.setVariable("typeOfExamination", examination.getPriceList().getTypeOfExamination().getName());

        DateTimeFormatter df = DateTimeFormat.forPattern("dd MM yyyy HH:mm:ss.SSS Z");
        DateTime temp = new DateTime();
        DateTimeZone theZone = DateTimeZone.getDefault();
        DateTimeFormatter df2 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSZZ");
        DateTimeFormatter df3 = df2.withZone(theZone);
        String dateTime = examination.getDateTime().withZone(theZone).toString();

        context.setVariable("date", dateTime.substring(0,10));
        context.setVariable("time", dateTime.substring(11));
        context.setVariable("roomNumber", examination.getRoom().getNumber());
        context.setVariable("clinicName", examination.getRoom().getClinic().getName());
        context.setVariable("doctorName", doctor.getName());
        context.setVariable("doctorLastName", doctor.getLastName());
        context.setVariable("emailConfirmLink", configProperties.getFrontBaseUrl() + "/examination/confirm-examination?token=" + tokenProvider.generateExaminationConfirmationToken(examination.getExaminationId()));
        context.setVariable("patient", user.getRole().getRoleId() != 5);
        sendMail(user.getEmail(), configProperties.getExaminationConfirmed(), context, "email_confirmation_examination.html");
    }
}
