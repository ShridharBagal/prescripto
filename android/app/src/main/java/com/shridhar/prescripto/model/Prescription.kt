package com.shridhar.prescripto.model

import java.util.UUID

data class Prescription(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val doctorId: String,
    val doctorName: String,
    val patientName: String,
    val dateIssued: Long,
    val medications: List<Medication>,
    val notes: String = "",
    val followUpDate: Long? = null
)
