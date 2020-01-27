package com.example.backend.service.impl;

import com.example.backend.dto.JwtAuthDto;
import com.example.backend.dto.request.DoctorFreeTermsRequestDTO;
import com.example.backend.dto.request.LoginRequestDTO;
import com.example.backend.dto.request.RegisterRequestDTO;
import com.example.backend.dto.response.DoctorFreeTermsResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.exception.*;
import com.example.backend.model.*;
import com.example.backend.repository.CityRepository;
import com.example.backend.repository.ClinicRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.UserService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
            if(user.getClinic() != null)
                return new JwtAuthDto(jwt, user.getRole().getName(), user.getClinic().getClinicId(), user.getClinic().getName());
            else
                return new JwtAuthDto(jwt, user.getRole().getName(), null, null);
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
        final Role userRole = roleRepository.findById(registerRequestDTO.getRoleId()).orElseThrow(() -> new ApiException("User Role not set."));

        // Create address for user
        final City city = cityRepository.findById(registerRequestDTO.getCityId()).orElseThrow(() -> new ApiException("City not set!"));

        // Creating user's account
        User user = new User(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(userRole);
        user.setCity(city);
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

    @Override
    public List<DoctorFreeTermsResponseDTO> getDoctorFreeTerms(DoctorFreeTermsRequestDTO doctorFreeTermsRequestDTO) {

        List<DoctorFreeTermsResponseDTO> freeTerms = new ArrayList<>();
        final User doctor = userRepository.findById(doctorFreeTermsRequestDTO.getDoctorId())
                .orElseThrow(() -> new UserNotFoundException("No user with given username or email found!"));
        LocalDate dateIterator = doctorFreeTermsRequestDTO.getDateTime().toLocalDate();

        //Find doctor schedules
        List<Schedule> schedules = doctor.getSchedules()
                .stream()
                .filter(s -> s.getEndDateSchedule().isAfter(doctorFreeTermsRequestDTO.getDateTime().toLocalDate()))
                .collect(Collectors.toList());

        //Find free terms on each day of the schedule
        for(Schedule schedule : schedules) {
            while(dateIterator.isBefore(schedule.getEndDateSchedule()) || dateIterator.isEqual(schedule.getEndDateSchedule())) {
                LocalDate date = dateIterator;

                //define start and end dateTime for shift
                DateTime startTime = createDateTime(date, schedule.getShiftStartTime());
                DateTime endTime;

                //if shift end next, day add 1 day to date
                if(schedule.getShiftEndTime().isAfter(schedule.getShiftStartTime())) {
                    endTime = createDateTime(date, schedule.getShiftEndTime());
                }
                else {
                    endTime = createDateTime(date.plusDays(1), schedule.getShiftEndTime());
                }

                //copy values  to new variables because variables used in lambda expressions should be final or effectively final. Same thing with date and dateIterator
                DateTime startTimeFinal = startTime;
                DateTime endTimeFinal = endTime;

                //Get doctors examinations in shift
                List<Examination> examinations = doctor.getDoctorExaminations()
                        .stream()
                        .filter(e -> (e.getDateTime().isAfter(startTimeFinal) || e.getDateTime().isEqual(startTimeFinal)) && e.getDateTime().isBefore(endTimeFinal))
                        .collect(Collectors.toList());
                examinations.sort(Comparator.comparing(Examination::getDateTime));

                //If there is no examinations on DATE, then every TERM on that DATE is free.
                //If there is examinations, then check if there is enough time between two examinations,
                // between START TIME of shift and first examination
                // or between last examination and END TIME od shift.
                // FOR loop find terms between two examinations and between START TIME of shift and first examination, if they exist.
                if(examinations.size() != 0) {
                    for (Examination examination : examinations) {
                        if (examination.getEndTime().toLocalTime().getMillisOfDay() - startTime.getMillisOfDay() > doctorFreeTermsRequestDTO.getDuration().getMillisOfDay()) {
                            freeTerms.add(new DoctorFreeTermsResponseDTO(startTime, examination.getDateTime().minus(doctorFreeTermsRequestDTO.getDuration().getMillisOfDay()).minusMinutes(1)));
                        }
                        startTime = examination.getEndTime().plusMinutes(1);
                    }
                    //Check if there is free term between last examination and end time.
                    if (endTime.getMillisOfDay() - startTime.getMillisOfDay() > doctorFreeTermsRequestDTO.getDuration().getMillisOfDay()) {
                        freeTerms.add(new DoctorFreeTermsResponseDTO(startTime, endTime.minus(doctorFreeTermsRequestDTO.getDuration().getMillisOfDay()).minusMinutes(1)));
                    }
                }
                else {
                    freeTerms.add(new DoctorFreeTermsResponseDTO(startTime, endTime.minus(doctorFreeTermsRequestDTO.getDuration().getMillisOfDay()).minusMinutes(1)));
                }
                dateIterator = date.plusDays(1);
            }
        }

        return freeTerms;
    }

    @Override
    public List<UserResponseDTO> getClinicMedicalStaff(Integer clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ClinicNotFoundException("Could not find clinic with given id"));
        List<UserResponseDTO> medicalStaff = clinic.getUsers()
                .stream()
                .filter( u -> (u.getRole().getRoleId() ==3 || u.getRole().getRoleId() == 4) && !u.isDeleted())
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        for (UserResponseDTO user : medicalStaff) {
            if(user.getRoleId() == 3) {
                user.setDoctorGrade(doctorGrade(user));
            }
        }
        return medicalStaff;
    }

    public static DateTime createDateTime(LocalDate date, LocalTime time) {
        return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour());
    }

    public double doctorGrade(UserResponseDTO user) {
        List<Examination> examinations = findByUsername(user.getUsername()).getDoctorExaminations()
                .stream()
                .filter(e -> e.getGradeDoctor() >= 1)
                .collect(Collectors.toList());
        if (examinations.size() > 0) {
            return examinations
                    .stream()
                    .map(e -> e.getGradeDoctor())
                    .collect(Collectors.summingDouble(Double::doubleValue))
                    /
                    examinations
                            .stream()
                            .collect(Collectors.toList())
                            .size();
        } else {
            return 0;
        }
    }


}
