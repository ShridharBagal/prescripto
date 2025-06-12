package com.shridhar.prescripto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shridhar.prescripto.model.Prescription
import com.shridhar.prescripto.model.UserRole
import com.shridhar.prescripto.network.ApiClient
import com.shridhar.prescripto.network.UserService
import com.shridhar.prescripto.ui.AddPrescriptionScreen
import com.shridhar.prescripto.ui.AuthScreen
import com.shridhar.prescripto.ui.DoctorHomeScreen
import com.shridhar.prescripto.ui.DoctorPrescriptionHistoryScreenWrapper
import com.shridhar.prescripto.ui.OtpEntryScreen
import com.shridhar.prescripto.ui.PatientHomeScreen
import com.shridhar.prescripto.ui.PatientPrescriptionScreenWrapper
import com.shridhar.prescripto.ui.QrScannerScreen
import com.shridhar.prescripto.ui.Screen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


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

            val userService = ApiClient.retrofit.create(UserService::class.java)
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "auth"){
                composable(Screen.Auth.route){
                    AuthScreen(
                        isSignUp = isSignUp,
                        onToggleMode = { isSignUp = !isSignUp },
                        onSubmit = { number, selectedRole ->
                            phone = number
                            role = selectedRole
                            navController.navigate(Screen.Otp.route)
                        }
                    )
                }

                composable(Screen.Otp.route){
                    OtpEntryScreen(
                        phoneNumber = phone,
                        onOtpSubmit = { otp ->
                            coroutineScope.launch {
                                try {
                                    val response = userService.getUserById(phone)
                                    if (response.isSuccessful) {
                                        val user = response.body()
                                        if (user != null) {
                                            role = if (user.role.equals("Doctor")) UserRole.Doctor else UserRole.Patient
                                            doctorName = if (role == UserRole.Doctor) user.name else doctorName
                                            doctorId = if (role == UserRole.Doctor) user.id else doctorId
                                            loggedInPatientName = if (role == UserRole.Patient) user.name else loggedInPatientName
                                            loggedInPatientId = if (role == UserRole.Patient) user.id else loggedInPatientId

                                            if (role == UserRole.Doctor)
                                                navController.navigate(Screen.DoctorHome.route)
                                            else
                                                navController.navigate(Screen.PatientHome.route)
                                        } else {
                                            navController.navigate(Screen.Auth.route)
                                        }
                                    } else {
                                        navController.navigate(Screen.Auth.route)
                                    }
                                } catch (e: Exception) {
                                    navController.navigate(Screen.Auth.route)
                                }
                            }
                        },
                        onBackToLogin = {
                            navController.navigate(Screen.Otp.route)
                        }
                    )

                }

                composable(Screen.DoctorHome.route){
                    DoctorHomeScreen(
                        onScanClick = { navController.navigate(Screen.Scanner.route) },
                        onHistoryClick = {
                            navController.navigate(Screen.DoctorHistory.route)
                        }
                    )
                }

                composable(Screen.PatientHome.route){
                    PatientHomeScreen(
                        patientId = loggedInPatientId,
                        onViewPrescriptions = { navController.navigate(Screen.PatientPrescriptions.route) }
                    )

                }
                composable(Screen.Scanner.route){
                    QrScannerScreen(
                        onScanResult = { (patientId, patientName) ->
                            scannedPatientId = patientId
                            scannedPatientName = patientName
                            navController.navigate(Screen.AddPrescription.route)
                        },
                        onBack = {
                            navController.navigate(Screen.DoctorHome.route)
                        }
                    )
                }

                composable(Screen.AddPrescription.route){
                    AddPrescriptionScreen(
                        patientId = scannedPatientId ?: "",
                        patientName = scannedPatientName,
                        doctorId = doctorId,
                        doctorName = doctorName,
                        onBack = { navController.navigate(Screen.DoctorHome.route) },
                    )
                }
                composable(Screen.PatientPrescriptions.route){
                    PatientPrescriptionScreenWrapper(
                        patientId = loggedInPatientId,
                        patientName = loggedInPatientName,
                        onBack = { navController.navigate(Screen.PatientHome.route) }
                    )
                }

                composable(Screen.DoctorHistory.route){
                    DoctorPrescriptionHistoryScreenWrapper(
                        doctorId =  doctorId,
                        onBack = { navController.navigate(Screen.DoctorHome.route) }
                    )
                }
            }
        }
    }

}
