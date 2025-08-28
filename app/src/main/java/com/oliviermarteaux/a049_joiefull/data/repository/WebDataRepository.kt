package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger

class WebDataRepository(
    private val apiServiceGetData: suspend () -> List<ItemDto>,
    private val mapper: (ItemDto) -> Item,
    private val log: Logger
) : DataRepository<Item> {

    private var cache: List<Item>? = null

    override suspend fun getData(): Result<List<Item>> = try {
        log.d("WebDataRepository: Api call successful")
        cache = apiServiceGetData().map(mapper)
        Result.success(cache!!)
//        Result.success(apiServiceGetData().map(mapper))
    } catch (e: Exception) {
        log.d("WebDataRepository: Api call failed. Reason: ${e.message}")
        Result.failure(e)
    }

    override fun getItemById(id: Int): Item? {
        return cache?.find { it.id == id }
    }
}