package com.shridhar.prescripto.model

enum class UserRole {
    Doctor,
    Patient;

    fun displayName(): String = when (this) {
        Doctor -> "Doctor"
        Patient -> "Patient"
    }

    companion object {
        fun fromString(role: String): UserRole? = when (role.lowercase()) {
            "doctor" -> Doctor
            "patient" -> Patient
            else -> null
        }
    }
}