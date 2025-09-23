package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class WebDataRepository(
    private val apiServiceGetData: suspend () -> List<ItemDto>,
    private val apiServicePutData: suspend (ItemDto) -> ItemDto,
    private val dtoToDomain: (ItemDto) -> Item,
    private val domainToDto: (Item) -> ItemDto,
) : DataRepository<Item> {

    private val itemsFlow = MutableStateFlow<List<Item>>(emptyList())

    override suspend fun getDataStream(): Result<Flow<List<Item>>> = try {
        val items = apiServiceGetData().map(dtoToDomain)
        itemsFlow.value = items
        Result.success(itemsFlow)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getItemByIdStream(id: Int): Flow<Item> {
        return itemsFlow.map { list -> list.first { it.id == id } }
    }

    override suspend fun updateItem(item: Item): Item = try {
        val updatedItemDto = apiServicePutData(domainToDto(item))
        val updatedItem = dtoToDomain(updatedItemDto)
        itemsFlow.value = itemsFlow.value.map { if (it.id == updatedItem.id) updatedItem else it }
        updatedItem
    } catch (e: Exception) {
        throw e
    }
}