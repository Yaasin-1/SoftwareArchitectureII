package com.example.part1.controller;

import com.example.part1.domain.Appointments;
import com.example.part1.domain.Doctor;
import com.example.part1.domain.ErrorInfo;
import com.example.part1.repo.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorRestController {

    @Autowired
    DoctorRepo doctorRepo;

    /* retrieve all doctors */
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        if (doctors.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Doctor>>(doctors, HttpStatus.OK);
    }

    /*Create a new doctor */
    @PostMapping("/doctors")
    public ResponseEntity<?> createPatient(@RequestBody Doctor doctor, UriComponentsBuilder ucBuilder) {
        if (doctorRepo.existsById(doctor.getId())){
            return new ResponseEntity(new ErrorInfo("This doctor named " + doctor.getName() + " with ID "+doctor.getId() + " already exists."), HttpStatus.CONFLICT);
        }
        doctorRepo.save(doctor);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/patients/{id}").buildAndExpand(doctor.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /*Get a specific doctor*/
    @GetMapping("/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable("id") Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return new ResponseEntity(new ErrorInfo("Doctor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Doctor>(doctor, HttpStatus.OK);
    }

    /*Update a specific doctor by id*/
    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor, @PathVariable("id") Long id) {
        Doctor currentDoctor = doctorRepo.findById(id).orElse(null);
        if (currentDoctor == null) {
            return new ResponseEntity(new ErrorInfo("Doctor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        currentDoctor.setName(doctor.getName());
        currentDoctor.setSpecialisation(doctor.getSpecialisation());
        currentDoctor.setEmail(doctor.getEmail());
        currentDoctor.setPhoneNumber(doctor.getPhoneNumber());
        currentDoctor.setAppointments(doctor.getAppointments());
        return new ResponseEntity<Doctor>(currentDoctor, HttpStatus.OK);
    }

    /*Delete a specific doctor*/
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("id") Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return new ResponseEntity(new ErrorInfo("Doctor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        doctorRepo.delete(doctor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*List all appointments for a specific doctor*/
    @GetMapping("/doctors/{id}/appointments")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable("id") Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return new ResponseEntity(new ErrorInfo("Doctor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        List<Appointments> appointments = doctor.getAppointments();
        if (appointments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
    }

}
