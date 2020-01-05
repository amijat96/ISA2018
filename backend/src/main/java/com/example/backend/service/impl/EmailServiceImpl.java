package com.example.backend.service.impl;

import com.example.backend.config.ConfigProperties;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.EmailService;
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

    @Autowired
    public EmailServiceImpl(ConfigProperties configProperties, UserRepository userRepository,
                            JavaMailSender javaMailSender, ServletContext servletContext,
                            JwtTokenProvider tokenProvider, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.servletContext = servletContext;
        this.configProperties = configProperties;
        this.tokenProvider = tokenProvider;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @Async
    void sendMail(String email, String subject, Context context) {
        final String body = templateEngine.process("email_confirmation.html", context);

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
    public void sendConfirmationMailToUser(int id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
        logger.info(String.format("Sending confirmation token to %s email.", user.getEmail()));
        Context context = new Context();
        context.setVariable("title", configProperties.getConfirmSubject());
        context.setVariable("firstName", user.getName());
        context.setVariable("emailConfirmLink", configProperties.getFrontBaseUrl() + "/confirm-email?token=" + tokenProvider.generateConfirmationToken(user.getUserId()));

        sendMail(user.getEmail(), configProperties.getConfirmSubject(), context);
    }
}
