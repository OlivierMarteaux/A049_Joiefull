package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.model.Item
import com.oliviermarteaux.shared.utils.Logger

interface ItemRepository {

    suspend fun getItems(): Result<List<Item>>
}

class WebItemRepository(
    private val itemApiService: ItemApiService,
    private val log: Logger
) : ItemRepository {
    override suspend fun getItems(): Result<List<Item>> = try {
        log.d("clothes Api call successful")
        Result.success(itemApiService.getItems())
    } catch (e: Exception) {
        log.d("Fallback failed. Reason: ${e.message}")
        Result.failure(e)
    }

}