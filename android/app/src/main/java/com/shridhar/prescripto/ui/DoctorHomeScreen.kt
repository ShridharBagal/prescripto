package com.shridhar.prescripto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DoctorHomeScreen(onScanClick: () -> Unit, onHistoryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Doctor Dashboard")
        Spacer(Modifier.height(32.dp))

        Button(onClick = onScanClick, modifier = Modifier.fillMaxWidth()) {
            Text("Scan Patient QR")
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth()) {
            Text("Prescription History")
        }
    }
}

