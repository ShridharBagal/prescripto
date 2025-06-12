package com.shridhar.prescripto.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shridhar.prescripto.model.Prescription
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DoctorPrescriptionHistoryScreenWrapper(
    doctorId: String,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val prescriptionService = remember {
        com.shridhar.prescripto.network.ApiClient.retrofit.create(
            com.shridhar.prescripto.network.PrescriptionService::class.java
        )
    }

    var prescriptions by remember { mutableStateOf<List<Prescription>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(doctorId) {
        coroutineScope.launch {
            try {
                val response = prescriptionService.getPrescriptionsForDoctor(doctorId)
                if (response.isSuccessful) {
                    prescriptions = response.body().orEmpty()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }

    when {
        isLoading -> {
            androidx.compose.material3.CircularProgressIndicator()
        }
        errorMessage != null -> {
            Text("Error: $errorMessage")
        }
        else -> {
            DoctorPrescriptionHistoryScreen(
                doctorId = doctorId,
                prescriptions = prescriptions,
                onBack = onBack
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPrescriptionHistoryScreen(
    doctorId: String,
    prescriptions: List<Prescription>,
    onBack: () -> Unit
) {
    val doctorPrescriptions = prescriptions.filter { it.doctorId == doctorId }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Prescriptions") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (doctorPrescriptions.isEmpty()) {
                item {
                    Text("You have not issued any prescriptions yet.")
                }
            } else {
                items(doctorPrescriptions) { prescription ->
                    DoctorPrescriptionCard(prescription)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun DoctorPrescriptionCard(prescription: Prescription) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(prescription.dateIssued))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Patient: ${prescription.patientName}")
            Text("Issued: $formattedDate")

            Spacer(modifier = Modifier.height(8.dp))

            prescription.medications.forEach { med ->
                Text("${med.name} ${med.dosage} (${med.frequency}, ${med.duration})")
            }

            if (prescription.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(" ${prescription.notes}")
            }
        }
    }
}

