package com.example.part1.controller;

import com.example.part1.domain.Patient;
import com.example.part1.repo.PatientRepo;
import com.example.part1.domain.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PatientRestController {

    @Autowired
    PatientRepo patientRepo;

    /* retrieve all patients */
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> listAllPatients() {
        List<Patient> patients = patientRepo.findAll();
        if (patients.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Patient>>(patients, HttpStatus.OK);
    }

    /* retrieve a specific patient by id */
    @GetMapping("/patients/{id}")
    public ResponseEntity<?> getHotel(@PathVariable("id") Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity(new ErrorInfo("Patient with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Patient>(patient, HttpStatus.OK);
    }
}
