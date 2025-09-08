package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto

interface ItemApiService {

    suspend fun getItems(): List<ItemDto>

    suspend fun getItemById(id: Int): ItemDto

    suspend fun updateItem(itemDto: ItemDto): ItemDto
}