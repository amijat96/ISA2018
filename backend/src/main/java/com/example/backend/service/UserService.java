package com.example.backend.service;

import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.model.User;

public interface UserService {

    JwtAuthDto login(LoginRequestDTO loginRequestDto);

    User register(RegisterRequestDTO registerRequestDto);

    Boolean confirmMail(String confirmToken);

    User findByUsername(String username);

    Boolean approveRegistration(int id);

}
