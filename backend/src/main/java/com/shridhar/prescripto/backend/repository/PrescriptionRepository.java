package com.shridhar.prescripto.backend.repository;

import com.shridhar.prescripto.backend.model.Prescription;
import com.shridhar.prescripto.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByDoctorId(String doctor);
    List<Prescription> findByPatientId(String patient);
}
