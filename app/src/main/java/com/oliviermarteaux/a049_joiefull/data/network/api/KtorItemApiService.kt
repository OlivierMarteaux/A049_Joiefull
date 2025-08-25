package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.shared.utils.CLOTHES_API_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class KtorItemApiService( // ✅ New implementation using Ktor
    private val client: HttpClient
) : ItemApiService {

    private val json = Json { ignoreUnknownKeys = true }

    // ✅ Replace Retrofit call with Ktor GET + deserialization
    override suspend fun getItems(): List<ItemDto> {
        val response =  client.get("${CLOTHES_API_URL}clothes.json")
        val bodyAsText = response.bodyAsText()
        return json.decodeFromString(bodyAsText)
    }
}