package com.shridhar.prescripto.model

data class Medication(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val instructions: String = ""
)
