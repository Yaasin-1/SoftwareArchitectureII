package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Patient;
import com.example.part1.domain.Record;
import com.example.part1.repo.PatientRepo;
import com.example.part1.domain.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientRestController {

    @Autowired
    PatientRepo patientRepo;

    /* retrieve all patients */
    @GetMapping
    public ResponseEntity<List<Patient>> listAllPatients() {
        List<Patient> patients = patientRepo.findAll();
        if (patients.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Patient>>(patients, HttpStatus.OK);
    }

    /* retrieve a specific patient by id */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable("id") Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Patient>(patient, HttpStatus.OK);
    }

    /*Create a new patient */
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient, UriComponentsBuilder ucBuilder) {
        if (patient.getId()!=null && patientRepo.existsById(patient.getId())){
            return new ResponseEntity(new ErrorInfo("This patient named " + patient.getName() + " with ID "+patient.getId() + " already exists."), HttpStatus.CONFLICT);
        }
        patientRepo.save(patient);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/patients/{id}").buildAndExpand(patient.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /*Update a specific patient*/
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody Patient patient, @PathVariable("id") Long id) {
        Patient currentPatient = patientRepo.findById(id).orElse(null);
        if (currentPatient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        currentPatient.setName(patient.getName());
        currentPatient.setAddress(patient.getAddress());
        currentPatient.setEmail(patient.getEmail());
        currentPatient.setPhoneNumber(patient.getPhoneNumber());
        patientRepo.save(currentPatient);
        return new ResponseEntity<Patient>(currentPatient, HttpStatus.OK);
    }

    /*Delete a specific patient*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable("id") Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        patientRepo.delete(patient);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*List all appointments for a specific patient*/
    @GetMapping("/{id}/appointments")
    public ResponseEntity<?> getAppointmentsFromPatient(@PathVariable("id") Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        List<Appointments> appointments = patient.getAppointments();
        if (appointments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
    }

    /*List all medical records for a specific patient*/
    @GetMapping("/{id}/medical-records")
    public ResponseEntity<?> getMedicalRecords(@PathVariable("id") Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        List<Record> records = patient.getRecords();
        if (records.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Record>>(records, HttpStatus.OK);
    }
}
