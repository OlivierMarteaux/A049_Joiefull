package com.oliviermarteaux.a049_joiefull.data

import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.shared.retrofit.WebDataRepository
import com.oliviermarteaux.shared.utils.AndroidLogger
import com.oliviermarteaux.shared.utils.Logger

class WebAppContainer(
    private val itemApiService: ItemApiService,
    private val log: Logger
): AppContainer {

//    private val itemApiService: ItemApiService by lazy {
//        RetrofitFactory.createFromUrl(CLOTHES_API_URL).create(ItemApiService::class.java)
//    }

    override val itemRepository: DataRepository<Item> by lazy {
        WebDataRepository(
            apiServiceGetData = itemApiService::getItems,
            mapper = {itemDto -> itemDto.toDomain()},
            log = log
        )
    }
}