package com.shridhar.prescripto.backend.repository;

import com.shridhar.prescripto.backend.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
