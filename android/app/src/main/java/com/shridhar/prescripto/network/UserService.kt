package com.shridhar.prescripto.network

import com.shridhar.prescripto.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>
}