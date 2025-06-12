package com.shridhar.prescripto.model

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val role: Role
)

enum class Role {
    DOCTOR, PATIENT
}