package com.example.part1.controller;

import com.example.part1.domain.*;
import com.example.part1.domain.Record;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.repo.DoctorRepo;
import com.example.part1.repo.PatientRepo;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentRestController {

    @Autowired
    AppointmentRepo appointmentRepo;

    @Autowired
    DoctorRepo doctorRepo;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    RecordRepo recordRepo;

    /*Get all appointments*/
    @GetMapping
    public ResponseEntity<List<Appointments>> listAllAppointments() {
        List<Appointments> appointments = appointmentRepo.findAll();
        if (appointments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
    }

    /*Create a new appointment*/
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointments appointment, UriComponentsBuilder ucBuilder) {
        if (appointment.getId()!=null&& appointmentRepo.existsById(appointment.getId())){
            return new ResponseEntity(new ErrorInfo("The appointment with ID "+appointment.getId() + " already exists."), HttpStatus.CONFLICT);
        }
        appointmentRepo.save(appointment);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/appointments/{id}").buildAndExpand(appointment.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /*Get a specific appointment by ID*/
    @GetMapping("/{id}")
    public ResponseEntity<Appointments> getAppointmentById(@PathVariable Long id) {
        Appointments appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) {
            return new ResponseEntity(new ErrorInfo("Appointment with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Appointments>(appointment, HttpStatus.OK);
    }

    /*Update a specific appointment*/
    @PutMapping("/{id}")
    public ResponseEntity<Appointments> updateAppointment(@PathVariable Long id, @RequestBody Appointments appointment) {
        Appointments currentAppointment = appointmentRepo.findById(id).orElse(null);
        if (currentAppointment == null) {
            return new ResponseEntity(new ErrorInfo("Appointment with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        //Validation logic in case a doctor/patient with a non existing ID is put through here
        Doctor currentDoctor = doctorRepo.findById(appointment.getDoctor().getId()).orElse(null);
        if (currentDoctor == null) {
            return new ResponseEntity(new ErrorInfo("Doctor with id " + appointment.getDoctor().getId() + " not found"), HttpStatus.NOT_FOUND);
        }
        else{
            currentAppointment.setDoctor(currentDoctor);
        }

        Patient currentPatient = patientRepo.findById(appointment.getPatient().getId()).orElse(null);
        if (currentPatient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + appointment.getPatient().getId() + " not found"), HttpStatus.NOT_FOUND);
        }
        else{
            currentAppointment.setDoctor(currentDoctor);
        }

        Record currentRecord = recordRepo.findById(appointment.getRecord().getId()).orElse(null);
        if (currentRecord == null) {//This just makes an empty record if there isn't an existing record for the patient
            Record newRecord = new Record();
            currentAppointment.setRecord(newRecord);
        }
        else{
            currentAppointment.setRecord(currentRecord);
        }
        currentAppointment.setAppointmentDate(appointment.getAppointmentDate());
        currentAppointment.setNotes(appointment.getNotes());
        currentAppointment.setStatus(appointment.getStatus());
        appointmentRepo.save(currentAppointment);
        return new ResponseEntity<Appointments>(currentAppointment, HttpStatus.OK);
    }

    /*Delete a specific appointment*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        Appointments appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) {
            return new ResponseEntity(new ErrorInfo("Appointment with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        appointmentRepo.delete(appointment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Get the specific medical record for the appointment*/
    @GetMapping("/{id}/medical-record")
    public ResponseEntity<Record> getMedicalRecordByAppointment(@PathVariable Long id) {
        Appointments appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) {
            return new ResponseEntity(new ErrorInfo("Appointment with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        Record medicalRecord = appointment.getRecord();
        if (medicalRecord == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Record>(medicalRecord, HttpStatus.OK);
    }

}
