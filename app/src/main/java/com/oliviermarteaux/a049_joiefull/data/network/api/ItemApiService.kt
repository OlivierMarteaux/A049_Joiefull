package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import retrofit2.http.GET

interface ItemApiService {

    @GET("{clothes}.json")
    suspend fun getItems(): List<ItemDto>
}