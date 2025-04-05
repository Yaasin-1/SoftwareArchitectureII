package com.example.part1.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String specialisation;
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    //The annotation below prevents the GetMapping controller from returning an infinite looping JSON
    @JsonManagedReference("doctor-appointments")//Specific ID's in place because I use more than one instance of this annotaion
    private List<Appointments> appointments;

    public Doctor() {}
    public Doctor(String name, String specialisation, String email, String phoneNumber) {
        this.name = name;
        this.specialisation = specialisation;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }
}
