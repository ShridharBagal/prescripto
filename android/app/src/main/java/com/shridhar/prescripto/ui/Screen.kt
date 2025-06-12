package com.shridhar.prescripto.ui

sealed class Screen(val route:String) {
    object Auth :Screen("auth")
    object Otp :Screen("otp")
    object DoctorHome :Screen("doctor")
    object PatientHome :Screen("patient")
    object Scanner :Screen("scanner")
    object AddPrescription :Screen("addPrescription")
    object PatientPrescriptions :Screen("patientPrescriptions")
    object DoctorHistory :Screen("doctorHistory")


}