package com.oliviermarteaux.shared.retrofit

import retrofit2.http.GET

interface TestApiService {
    @GET("/test")
    suspend fun getTest(): String
}