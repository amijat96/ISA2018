package com.example.backend.model;

import com.example.backend.dto.request.RegisterRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private int userId;

    @Version
    private long version;

    @NotNull
    @Column(name = "USERNAME")
    private String username;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "VERIFIED")
    private boolean verified;

    @Column(name = "DELETED")
    private boolean deleted;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "JBO")
    private String jbo;

    @Column(name = "PASSWORD_CHANGED")
    private  boolean passwordChanged;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "STREET")
    private String street;

    // 0-male, 1-female, 2-other
    @Column(name = "GENDER")
    private int gender;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "ADMIN_APPROVED")
    private boolean adminApproved;

    @ManyToOne
    @JoinColumn(name = "ID_ROLE")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "ID_CITY")
    private City city;

    @ManyToOne
    @JoinColumn(name = "ID_CLINIC")
    private Clinic clinic;

    @OneToOne
    @JoinColumn(name = "ID_MEDICAL_RECORD")
    private MedicalRecord medicalRecord;

    @OneToMany(mappedBy = "user")
    private List<Examination> examinations;

    @OneToMany(mappedBy = "doctor")
    private List<Examination> doctorExaminations;

    @ManyToMany
    @JoinTable(
            name = "doctorspecialization"
            , inverseJoinColumns={
            @JoinColumn(name="ID_TYPE_OF_EXAMINATION")
        }
            , joinColumns={
            @JoinColumn(name="ID_USER")
        }
    )
    private List<TypeOfExamination> doctorSpecialization;

    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "user")
    private List<Vacation> vacations;

    public User(RegisterRequestDTO registerRequestDto) {
        this.username = registerRequestDto.getUsername();
        this.password = registerRequestDto.getPassword();
        this.email = registerRequestDto.getEmail();
        this.name = registerRequestDto.getName();
        this.lastName = registerRequestDto.getLastName();
        this.gender = registerRequestDto.getGender();
        this.jbo = registerRequestDto.getJbo();
        this.phone = registerRequestDto.getPhone();
        this.street = registerRequestDto.getStreet();
        this.dateOfBirth = registerRequestDto.getDateOfBirth();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.verified && this.adminApproved;
    }
}
