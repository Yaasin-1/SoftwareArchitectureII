package com.example.part1.repo;

import com.example.part1.domain.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointments,Long> {
    Optional<Appointments> findByPatientId(Long patientId);
    Optional<Appointments> findByDoctorId(Long doctorId);
}
