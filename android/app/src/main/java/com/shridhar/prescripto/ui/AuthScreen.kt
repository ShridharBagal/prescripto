package com.shridhar.prescripto.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shridhar.prescripto.model.UserRole


@Composable
fun AuthScreen(
    isSignUp: Boolean,
    onToggleMode: () -> Unit,
    onSubmit: (String, UserRole?) -> Unit
) {
    var phone by remember { mutableStateOf("") }

    var selectedRole by remember {
        mutableStateOf(if (isSignUp) UserRole.Patient else null)
    }

    LaunchedEffect(isSignUp) {
        if (isSignUp && selectedRole == null) {
            selectedRole = UserRole.Patient
        }
        if (!isSignUp) {
            selectedRole = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isSignUp) "Sign Up" else "Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10) phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (isSignUp) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Select Role:")

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRole == UserRole.Doctor,
                    onClick = { selectedRole = UserRole.Doctor }
                )
                Text("Doctor", modifier = Modifier.padding(end = 16.dp))

                RadioButton(
                    selected = selectedRole == UserRole.Patient,
                    onClick = { selectedRole = UserRole.Patient }
                )
                Text("Patient")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSubmit(phone, selectedRole) },
            enabled = phone.length >= 10 && (!isSignUp || selectedRole != null),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send OTP")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onToggleMode) {
            Text(if (isSignUp) "Already have an account? Login" else "Don't have an account? Sign Up")
        }
    }
}


