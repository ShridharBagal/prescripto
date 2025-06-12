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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.shridhar.prescripto.network.ApiClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PatientPrescriptionScreenWrapper(
    patientId: String,
    patientName: String,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val prescriptionService = remember { ApiClient.retrofit.create(com.shridhar.prescripto.network.PrescriptionService::class.java) }

    var prescriptions by remember { mutableStateOf<List<Prescription>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(patientId) {
        coroutineScope.launch {
            try {
                val response = prescriptionService.getPrescriptionsForPatient(patientId)
                if (response.isSuccessful) {
                    prescriptions = response.body().orEmpty()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (errorMessage != null) {
        Text("Failed: $errorMessage")
    } else {
        PatientPrescriptionScreen(
            prescriptions = prescriptions,
            patientName = patientName,
            onBack = onBack
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientPrescriptionScreen(
    prescriptions: List<Prescription>,
    patientName: String,
    onBack: () -> Unit
) {
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
            item{
                Text("Hello, $patientName")
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (prescriptions.isEmpty()) {
                item{
                    Text("No prescriptions found.")
                }
            } else {
                items(prescriptions) { prescription ->
                    PatientPrescriptionCard(prescription)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


@Composable
fun PatientPrescriptionCard(prescription: Prescription) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(prescription.dateIssued))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${prescription.doctorName}")
            Text("Issued on $formattedDate")

            Spacer(modifier = Modifier.height(8.dp))

            prescription.medications.forEach { med ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("${med.name} - ${med.dosage}")
                    Text("${med.frequency} × ${med.duration}")
                    if (med.instructions.isNotBlank())
                        Text("${med.instructions}")
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }

            if (prescription.notes.isNotBlank()) {
                Text("Notes: ${prescription.notes}")
            }

            prescription.followUpDate?.let { millis ->
                val followUp = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
                Text("Follow-up: $followUp")
            }
        }
    }
}

