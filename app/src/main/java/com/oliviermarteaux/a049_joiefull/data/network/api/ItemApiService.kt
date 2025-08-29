package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import io.ktor.client.utils.EmptyContent.contentType

interface ItemApiService {

//    @GET("{clothes}.json") // ✅ Pure suspend function, no Retrofit
    suspend fun getItems(): List<ItemDto>

    suspend fun updateItem(itemDto: ItemDto): ItemDto
}