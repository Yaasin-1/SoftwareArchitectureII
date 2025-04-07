package com.example.part1.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    //The models are set up so that patients has list appointments and appointment has attribute patient
    //When testing in postman, the GetMapping returns an infinite looping JSON
    //This @JsonManagedReference makes it so that it doesn't loop
    @JsonManagedReference("patient-appointments")
    private List<Appointments> appointments;

    public Patient() {}

    public Patient(String name, String email, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }
}
