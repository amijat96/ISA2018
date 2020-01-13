package com.example.backend.service.impl;

import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.exception.*;
import com.example.backend.model.City;
import com.example.backend.model.Clinic;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.CityRepository;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final CityRepository cityRepository;

    private final ClinicRepository clinicRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, CityRepository cityRepository,
                           PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider,
                           ClinicRepository clinicRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public JwtAuthDto login(LoginRequestDTO loginRequestDTO) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsernameOrEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final User user = userRepository.findByUsernameOrEmail(loginRequestDTO.getUsernameOrEmail(), loginRequestDTO.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("No user with given username or email found!"));
        if(user.isVerified() && user.isAdminApproved()) {
            String jwt = tokenProvider.generateAuthToken(authentication);
            return new JwtAuthDto(jwt, user.getRole().getName());
        } else if(!user.isVerified()){
            throw new EmailNotVerifiedException("Mail is not verified.");
        } else {
            throw new RegistrationNotApprovedException("Registration is not approved.");
        }
    }

    @Override
    public User register(RegisterRequestDTO registerRequestDTO) {
        if(userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Finds role
        final Role userRole = roleRepository.findById(registerRequestDTO.getRoleIdd()).orElseThrow(() -> new ApiException("User Role not set."));

        // Create address for user
        final City city = cityRepository.findById(registerRequestDTO.getCityId()).orElseThrow(() -> new ApiException("City not set!"));

        // Find clinic
        Clinic clinic = null;
        if(registerRequestDTO.getClinicId() != 0)
            clinic = clinicRepository.findById(registerRequestDTO.getClinicId()).orElseThrow(() -> new ApiException("Clinic not set!"));

        // Creating user's account
        User user = new User(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(userRole);
        user.setCity(city);
        if(clinic != null)
            user.setClinic(clinic);
        userRepository.save(user);

        return user;
    }

    @Override
    public Boolean confirmMail(String confirmToken) {
        if(tokenProvider.validateToken(confirmToken)) {
            final int userId = tokenProvider.getIdFromJwt(confirmToken);
            final User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found.", Integer.toString(userId))));
            user.setVerified(true);

            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean approveRegistration(int id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found.", Integer.toString(id))));
        user.setAdminApproved(true);

        userRepository.save(user);
        return true;
    }


}
