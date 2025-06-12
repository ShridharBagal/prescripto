package com.shridhar.prescripto.network

import com.shridhar.prescripto.model.Prescription
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PrescriptionService {
    @POST("prescriptions")
    suspend fun postPrescription(@Body prescription: Prescription): Response<String>

    @GET("prescriptions/patient/{patientId}")
    suspend fun getPrescriptionsForPatient(@Path("patientId") id: String): Response<List<Prescription>>

    @GET("prescriptions/doctor/{doctorId}")
    suspend fun getPrescriptionsForDoctor(@Path("doctorId") doctorId: String): Response<List<Prescription>>

}
