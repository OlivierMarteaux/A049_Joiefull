package com.oliviermarteaux.shared.retrofit

import retrofit2.http.GET

interface FakeApiService {
    @GET("/")
    suspend fun getData(): String
}