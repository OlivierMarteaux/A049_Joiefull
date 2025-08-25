package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.shared.utils.CLOTHES_API_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorItemApiService( // ✅ New implementation using Ktor
    private val client: HttpClient
) : ItemApiService {

    override suspend fun getItems(): List<ItemDto> {
        return client.get("${CLOTHES_API_URL}clothes.json").body()
        // ✅ Replace Retrofit call with Ktor GET + deserialization
    }
}