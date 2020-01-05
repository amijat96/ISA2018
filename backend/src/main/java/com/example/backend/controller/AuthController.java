package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.model.User;
import com.example.backend.service.EmailService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private final UserService userService;

    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthDto> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtAuthDto jwtToken = userService.login(loginRequestDTO);
        return ResponseEntity.ok(jwtToken);
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        final User user = userService.register(registerRequestDTO);
        return new ResponseEntity<>(new UserResponseDTO(user), HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/approve-registration/{id}")
    @PreAuthorize("hasRole('ADMIN_SYSTEM')")
    public ResponseEntity<ApiResponse> approveRegistration(@PathVariable(name = "id")int id) {
        if(userService.approveRegistration(id)) {
            emailService.sendConfirmationMailToUser(id);
            return ResponseEntity.ok(new ApiResponse(true, "Registration approved.", new ArrayList<>()));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Registration approval failed.", new ArrayList<>()));
        }
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse> confirmMail(@RequestParam("token") String confirmationToken) {
        boolean result = userService.confirmMail(confirmationToken);
        String message = "Email confirmation success";
        if(!result) {
            message = "Email confirmation failed";
            final List<String> errors = new ArrayList<>();
            errors.add(message);
            return new ResponseEntity<>(new ApiResponse(false, message, errors), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, message, new ArrayList<>()));
    }
}
