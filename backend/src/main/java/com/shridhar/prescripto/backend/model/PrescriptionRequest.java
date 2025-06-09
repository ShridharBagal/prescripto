package com.shridhar.prescripto.backend.model;

import java.util.List;

public record PrescriptionRequest(
        Long doctorId,
        Long patientId,
        List<MedicationRequest> medications
) {}
