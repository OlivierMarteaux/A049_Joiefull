package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger

class WebDataRepository(
    private val apiServiceGetData: suspend () -> List<ItemDto>,
    private val apiServicePutData: suspend (ItemDto) -> ItemDto,
    private val dtoToDomain: (ItemDto) -> Item,
    private val domainToDto: (Item) -> ItemDto,
    private val log: Logger
) : DataRepository<Item> {

    private lateinit var cache: List<Item>

    override suspend fun getData(): Result<List<Item>> = try {
        log.d("WebDataRepository: Api call successful")
        cache = apiServiceGetData().map(dtoToDomain)
        Result.success(cache)
//        Result.success(apiServiceGetData().map(mapper))
    } catch (e: Exception) {
        log.d("WebDataRepository: Api call failed. Reason: ${e.message}")
        Result.failure(e)
    }

    override fun getItemById(id: Int): Item {
        return cache.find { it.id == id }!!
    }

    override suspend fun updateItem(item: Item): Item = try {
        log.d("WebDataRepository: Api call successful")
        dtoToDomain(apiServicePutData(domainToDto(item)))
    } catch (e: Exception) {
        log.d("WebDataRepository: Api call failed. Reason: ${e.message}")
        throw e
    }
}