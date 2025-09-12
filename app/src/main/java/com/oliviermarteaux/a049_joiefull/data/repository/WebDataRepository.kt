package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class WebDataRepository(
    private val apiServiceGetData: suspend () -> List<ItemDto>,
    private val apiServicePutData: suspend (ItemDto) -> ItemDto,
    private val apiServiceGetById: suspend (Int) -> ItemDto,
    private val dtoToDomain: (ItemDto) -> Item,
    private val domainToDto: (Item) -> ItemDto,
    private val log: Logger
) : DataRepository<Item> {

//    private lateinit var cache: List<Item>
    private val itemsFlow = MutableStateFlow<List<Item>>(emptyList())

//    override suspend fun getData(): Result<List<Item>> = try {
//        log.d("WebDataRepository.getData(): Api call successful")
////        cache = apiServiceGetData().map(dtoToDomain)
//        val collectedItemList: List<Item> = apiServiceGetData().map(dtoToDomain)
//        Result.success(collectedItemList)
////        Result.success(apiServiceGetData().map(mapper))
//    } catch (e: Exception) {
//        log.d("WebDataRepository.getData(): Api call failed. Reason: ${e.message}")
//        Result.failure(e)
//    }

    override suspend fun getDataStream(): Result<Flow<List<Item>>> = try {
        log.d("WebDataRepository.getDataStream(): Api call successful")
        val items = apiServiceGetData().map(dtoToDomain)
        itemsFlow.value = items
        Result.success(itemsFlow)
    } catch (e: Exception) {
        log.d("WebDataRepository.getDataStream(): Api call failed. Reason: ${e.message}")
        Result.failure(e)
    }

    override suspend fun getItemById(id: Int): Item {
        log.d("WebDataRepository.getItemById: Api call successful")
        val collectedItem: Item = dtoToDomain(apiServiceGetById(id))
//        return cache.find { it.id == id }!!
        return collectedItem
    }

    override fun getItemByIdStream(id: Int): Flow<Item> {
        return itemsFlow.map { list -> list.first { it.id == id } }
    }

    override suspend fun updateItem(item: Item): Item = try {
        log.d("WebDataRepository.updateItem(${item.id}): Api call successful")
        val updatedItemDto = apiServicePutData(domainToDto(item))
//        cache = cache.map { if (it.id == updatedItem.id) dtoToDomain(updatedItem) else it }
        val updatedItem = dtoToDomain(updatedItemDto)
        itemsFlow.value = itemsFlow.value.map { if (it.id == updatedItem.id) updatedItem else it }
        updatedItem
    } catch (e: Exception) {
        log.d("WebDataRepository.updateItem: Api call failed. Reason: ${e.message}")
        throw e
    }
}