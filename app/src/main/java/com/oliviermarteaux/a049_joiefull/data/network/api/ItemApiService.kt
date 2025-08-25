package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto

interface ItemApiService {

//    @GET("{clothes}.json") // ✅ Pure suspend function, no Retrofit
    suspend fun getItems(): List<ItemDto>
}