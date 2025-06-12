package com.shridhar.prescripto.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shridhar.prescripto.model.Medication
import com.shridhar.prescripto.model.Prescription
import com.shridhar.prescripto.network.ApiClient
import com.shridhar.prescripto.network.PrescriptionService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPrescriptionScreen(
    patientId: String,
    patientName: String,
    doctorId: String,
    doctorName: String,
    onBack: () -> Unit,
) {
    val prescriptionService = ApiClient.retrofit.create(PrescriptionService::class.java)
    val coroutineScope = rememberCoroutineScope()


    var notes by remember { mutableStateOf("") }
    var followUpDate by remember { mutableStateOf("") }
    val medications = remember { mutableStateListOf<Medication>() }

    var medName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Prescription") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Patient: $patientName")
            Text("Patient ID: $patientId")
            Spacer(modifier = Modifier.height(16.dp))

            Text("Add Medication", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(value = medName, onValueChange = { medName = it }, label = { Text("Medicine Name") })
            OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") })
            OutlinedTextField(value = frequency, onValueChange = { frequency = it }, label = { Text("Frequency") })
            OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration)") })
            OutlinedTextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Instructions") })

            Button(
                onClick = {
                    if (medName.isNotBlank() && dosage.isNotBlank()) {
                        medications.add(
                            Medication(
                                name = medName,
                                dosage = dosage,
                                frequency = frequency,
                                duration = duration,
                                instructions = instructions
                            )
                        )
                        medName = ""; dosage = ""; frequency = ""; duration = ""; instructions = ""
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Add to List")
            }

            medications.forEachIndexed { index, med ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${med.name} - ${med.dosage}")
                        Text("${med.frequency} × ${med.duration}")
                        if (med.instructions.isNotBlank())
                            Text("${med.instructions}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = followUpDate,
                onValueChange = { followUpDate = it },
                label = { Text("Follow-up Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val prescription = Prescription(
                        patientId = patientId,
                        doctorId = doctorId,
                        doctorName = doctorName,
                        patientName = patientName,
                        dateIssued = System.currentTimeMillis(),
                        medications = medications.toList(),
                        notes = notes,
                        followUpDate = followUpDate.toLongOrNull()
                    )

                    coroutineScope.launch {
                        val response = prescriptionService.postPrescription(prescription)
                        if (response.isSuccessful) {
                            Log.d("Submit", "Prescription submitted")
                            onBack()
                        } else {
                            Log.e("Submit", "Error: ${response.errorBody()?.string()}")
                        }
                    }
                },
                enabled = medications.isNotEmpty(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit Prescription")
            }
        }
    }
}
