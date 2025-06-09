package com.shridhar.prescripto.backend.model;

public record MedicationRequest(
        String name,
        String dosage,
        String frequency,
        int durationDays
) {}
