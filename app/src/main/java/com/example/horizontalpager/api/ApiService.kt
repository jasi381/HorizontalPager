package com.example.horizontalpager.api

import com.example.horizontalpager.data.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
