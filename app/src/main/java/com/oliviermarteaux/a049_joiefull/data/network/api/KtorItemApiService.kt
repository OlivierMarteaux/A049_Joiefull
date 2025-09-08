package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.shared.utils.CUSTOM_CLOTHES_API_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class KtorItemApiService(
    private val client: HttpClient
) : ItemApiService {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getItems(): List<ItemDto> {
        return client.get("${CUSTOM_CLOTHES_API_URL}clothes").body()
    }

    override suspend fun getItemById(id: Int): ItemDto {
        return client.get("${CUSTOM_CLOTHES_API_URL}/clothes/$id").body()
    }

    override suspend fun updateItem(itemDto: ItemDto): ItemDto {
        return client.put("${CUSTOM_CLOTHES_API_URL}/clothes/${itemDto.id}") {
            contentType(ContentType.Application.Json)
            setBody(itemDto) // ✅ send DTO
        }.body()
    }
}