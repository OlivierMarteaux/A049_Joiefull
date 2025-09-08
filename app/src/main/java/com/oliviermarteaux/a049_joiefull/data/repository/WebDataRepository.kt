package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger

class WebDataRepository(
    private val apiServiceGetData: suspend () -> List<ItemDto>,
    private val apiServicePutData: suspend (ItemDto) -> ItemDto,
    private val apiServiceGetById: suspend (Int) -> ItemDto,
    private val dtoToDomain: (ItemDto) -> Item,
    private val domainToDto: (Item) -> ItemDto,
    private val log: Logger
) : DataRepository<Item> {

//    private lateinit var cache: List<Item>

    override suspend fun getData(): Result<List<Item>> = try {
        log.d("WebDataRepository.getData(): Api call successful")
//        cache = apiServiceGetData().map(dtoToDomain)
        val collectedItemList: List<Item> = apiServiceGetData().map(dtoToDomain)
        Result.success(collectedItemList)
//        Result.success(apiServiceGetData().map(mapper))
    } catch (e: Exception) {
        log.d("WebDataRepository.getData(): Api call failed. Reason: ${e.message}")
        Result.failure(e)
    }

    override suspend fun getItemById(id: Int): Item {
        log.d("WebDataRepository.getItemById: Api call successful")
        val collectedItem: Item = dtoToDomain(apiServiceGetById(id))
//        return cache.find { it.id == id }!!
        return collectedItem
    }

    override suspend fun updateItem(item: Item): Item = try {
        log.d("WebDataRepository.updateItem: Api call successful")
        val updatedItem = apiServicePutData(domainToDto(item))
//        cache = cache.map { if (it.id == updatedItem.id) dtoToDomain(updatedItem) else it }
        dtoToDomain(updatedItem)
    } catch (e: Exception) {
        log.d("WebDataRepository.updateItem: Api call failed. Reason: ${e.message}")
        throw e
    }
}