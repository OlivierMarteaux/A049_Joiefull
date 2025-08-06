package com.oliviermarteaux.a049_joiefull.data.api

import com.oliviermarteaux.a049_joiefull.data.model.Item
import retrofit2.http.GET

interface ItemApiService {

    @GET("{clothes}.json")
    suspend fun getItems(): List<Item>
}