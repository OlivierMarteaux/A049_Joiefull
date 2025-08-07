package com.oliviermarteaux.a049_joiefull.data

import android.content.Context
import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.shared.data.WebDataRepository
import com.oliviermarteaux.shared.retrofit.RetrofitFactory
import com.oliviermarteaux.shared.utils.AndroidLogger
import com.oliviermarteaux.shared.utils.CLOTHES_API_URL
import kotlinx.coroutines.CoroutineScope

class AppApiContainer(
    private val context: Context,
    private val applicationScope: CoroutineScope
): AppContainer {
    private val itemApiService: ItemApiService by lazy {
        RetrofitFactory.createFromUrl(CLOTHES_API_URL).create(ItemApiService::class.java)
    }

    override val itemRepository: DataRepository<Item> by lazy {
        WebDataRepository(
            apiServiceGetData = itemApiService::getItems,
            mapper = {itemDto -> itemDto.toDomain()},
            log = AndroidLogger
        )
    }
}