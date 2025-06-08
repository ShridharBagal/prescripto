package com.shridhar.prescripto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.shridhar.prescripto.model.Medication
import com.shridhar.prescripto.model.Prescription
import com.shridhar.prescripto.model.UserRole
import com.shridhar.prescripto.ui.AddPrescriptionScreen
import com.shridhar.prescripto.ui.AuthScreen
import com.shridhar.prescripto.ui.DoctorHomeScreen
import com.shridhar.prescripto.ui.DoctorPrescriptionHistoryScreen
import com.shridhar.prescripto.ui.OtpEntryScreen
import com.shridhar.prescripto.ui.PatientHomeScreen
import com.shridhar.prescripto.ui.PatientPrescriptionScreen
import com.shridhar.prescripto.ui.QrScannerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var screen by remember { mutableStateOf("auth") }
            var isSignUp by remember { mutableStateOf(false) }
            var phone by remember { mutableStateOf("") }
            var role by remember { mutableStateOf<UserRole?>(null) }

            var doctorName by remember { mutableStateOf("Dr. Sharma") }
            var loggedInPatientName by remember { mutableStateOf("doc_123") }

            var scannedPatientId by remember { mutableStateOf<String?>(null) }
            var scannedPatientName by remember { mutableStateOf("Shridhar Bagal") }

            val prescriptions = remember { mutableStateListOf<Prescription>() }

            var loggedInPatientId by remember { mutableStateOf("shridhar") }
            var doctorId by remember { mutableStateOf("bagal") }

            val samplePrescriptions = listOf(
                Prescription(
                    patientId = "patient_001",
                    doctorId = "doc_123",
                    doctorName = "Dr. Sharma",
                    patientName = "Shridhar",
                    dateIssued = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000,
                    medications = listOf(
                        Medication(
                            name = "Crocin",
                            dosage = "500mg",
                            frequency = "1-0-1",
                            duration = "3 days",
                            instructions = "After meals"
                        ),
                        Medication(
                            name = "Paracetamol",
                            dosage = "650mg",
                            frequency = "1-1-1",
                            duration = "5 days",
                            instructions = "nil"
                        )
                    ),
                    notes = "fever",
                    followUpDate = System.currentTimeMillis() + 4 * 24 * 60 * 60 * 1000
                )
            )





            when (screen) {

                "auth" -> AuthScreen(
                    isSignUp = isSignUp,
                    onToggleMode = { isSignUp = !isSignUp },
                    onSubmit = { number, selectedRole ->
                        phone = number
                        role = selectedRole
                        screen = "otp"
                    }
                )

                "otp" -> OtpEntryScreen(
                    phoneNumber = phone,
                    onOtpSubmit = { otp ->
                        screen = when (role ?: UserRole.Doctor) {
                            UserRole.Doctor -> "doctor"
                            UserRole.Patient -> "patient"
                        }
                    },
                    onBackToLogin = {
                        screen = "auth"
                    }
                )

                "doctor" -> DoctorHomeScreen(
                    onScanClick = { screen = "scanner" },
                    onHistoryClick = {
                        screen = "doctorHistory"
                    }
                )

                "patient" -> PatientHomeScreen(
                    patientId = "phone",
                    onViewPrescriptions = {
                        screen = "patientPrescriptions"
                    }
                )

                "scanner" -> QrScannerScreen(
                    onScanResult = { patientId ->
                        Toast.makeText(this, "Scanned: $patientId", Toast.LENGTH_SHORT).show()
                        screen = "doctor"
                    },
                    onBack = {
                        screen = "doctor"
                    }
                )

                "addPrescription" -> AddPrescriptionScreen(
                    patientId = scannedPatientId ?: "",
                    patientName = scannedPatientName,
                    doctorId = doctorId,
                    doctorName = doctorName,
                    onBack = { screen = "doctor" },
                    onSubmit = { newPrescription ->
                        prescriptions.add(newPrescription)
                        screen = "doctor"
                    }
                )

//                "patientPrescriptions" -> PatientPrescriptionScreen(
//                    prescriptions = prescriptions.filter { it.patientId == loggedInPatientId },
//                    patientName = loggedInPatientName,
//                    onBack = { screen = "patient" }
//                )

                "patientPrescriptions" -> PatientPrescriptionScreen(
                    prescriptions = samplePrescriptions,
                    patientName = "Ravi Patel",
                    onBack = { screen = "patient" }
                )

//                "doctorHistory" -> DoctorPrescriptionHistoryScreen(
//                    doctorId = doctorId,
//                    prescriptions = prescriptions,
//                    onBack = { screen = "doctor" }
//                )

                "doctorHistory" -> DoctorPrescriptionHistoryScreen(
                    doctorId = "doc_123",
                    prescriptions = samplePrescriptions,
                    onBack = { screen = "doctor" }
                )

            }
        }
    }

}
