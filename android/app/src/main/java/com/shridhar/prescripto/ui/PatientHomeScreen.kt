package com.shridhar.prescripto.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.shridhar.prescripto.QrCodeUtil


@Composable
fun PatientHomeScreen(
    patientId: String,
    onViewPrescriptions: () -> Unit
) {
    val bitmap = remember(patientId) {
        QrCodeUtil.generateQrBitmap(patientId)
    }.asImageBitmap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Your QR Code", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Image(bitmap = bitmap, contentDescription = "QR Code")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onViewPrescriptions) {
            Text("View Prescriptions")
        }
    }
}
