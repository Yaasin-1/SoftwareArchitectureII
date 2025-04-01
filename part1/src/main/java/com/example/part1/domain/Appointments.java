package com.example.part1.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp appointmentDate;
    private String status;
    private String notes;

    @ManyToOne
    @JoinColumn()
    private Patient patient;

    @ManyToOne
    @JoinColumn()
    private Doctor doctor;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Record record;

    public Long getId() {
        return id;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public Record getRecord() {
        return record;
    }
    public void setRecord(Record record) {
        this.record = record;
    }

}
