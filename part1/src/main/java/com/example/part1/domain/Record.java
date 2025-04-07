package com.example.part1.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp recordDate;
    private String diagnosis;
    private String treatment;
    private String notes;

    @OneToOne
    @JsonBackReference("appointment-record")
    private Appointments appointment;

    public Record() {}
    public Record(Long id, Timestamp recordDate, String diagnosis, String treatment, String notes) {
        this.id = id;
        this.recordDate = recordDate;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }
    public Long getId() {
        return id;
    }

    public Timestamp getRecordDate() {
        return recordDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRecordDate(Timestamp recordDate) {
        this.recordDate = recordDate;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Appointments getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointments appointment) {
        this.appointment = appointment;
    }
}
