package com.shridhar.prescripto.backend.controller;

import com.shridhar.prescripto.backend.model.Prescription;
import com.shridhar.prescripto.backend.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @PostMapping
    public String createPrescription(@RequestBody Prescription prescription) {
        prescriptionRepository.save(prescription);
        return "Prescription saved";
    }

    @GetMapping("/patient/{patientId}")
    public List<Prescription> getPrescriptionsForPatient(@PathVariable String patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getPrescriptionsForDoctor(@PathVariable String doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}
