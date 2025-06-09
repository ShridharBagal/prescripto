package com.shridhar.prescripto.backend.controller;

import com.shridhar.prescripto.backend.model.*;
import com.shridhar.prescripto.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepo;
    private final UserRepository userRepo;
    private final MedicationRepository medicationRepo;

    @PostMapping
    public ResponseEntity<String> addPrescription(@RequestBody PrescriptionRequest request) {
        Optional<User> doctorOpt = userRepo.findById(request.doctorId());
        Optional<User> patientOpt = userRepo.findById(request.patientId());

        if (doctorOpt.isEmpty() || patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid doctor or patient ID.");
        }

        Prescription prescription = Prescription.builder()
                .doctor(doctorOpt.get())
                .patient(patientOpt.get())
                .dateIssued(LocalDateTime.now())
                .build();

        prescription = prescriptionRepo.save(prescription);

        for (MedicationRequest medReq : request.medications()) {
            Medication med = Medication.builder()
                    .name(medReq.name())
                    .dosage(medReq.dosage())
                    .frequency(medReq.frequency())
                    .durationDays(medReq.durationDays())
                    .prescription(prescription)
                    .build();
            medicationRepo.save(med);
        }

        return ResponseEntity.ok("Prescription saved.");
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsForPatient(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(patient -> ResponseEntity.ok(prescriptionRepo.findByPatient(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsForDoctor(@PathVariable Long id) {
        return userRepo.findById(id)
                .map(doctor -> ResponseEntity.ok(prescriptionRepo.findByDoctor(doctor)))
                .orElse(ResponseEntity.notFound().build());
    }
}
