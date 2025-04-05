package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.ErrorInfo;
import com.example.part1.repo.AppointmentRepo;
import com.example.part1.domain.Record;
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
        currentAppointment.setAppointmentDate(appointment.getAppointmentDate());
        currentAppointment.setNotes(appointment.getNotes());
        currentAppointment.setStatus(appointment.getStatus());
        currentAppointment.setDoctor(appointment.getDoctor());
        currentAppointment.setPatient(appointment.getPatient());
        currentAppointment.setRecord(appointment.getRecord());
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
