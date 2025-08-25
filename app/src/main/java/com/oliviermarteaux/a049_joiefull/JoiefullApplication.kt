package com.oliviermarteaux.a049_joiefull

import android.app.Application
import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.shared.data.AppContainer
import com.oliviermarteaux.shared.retrofit.RetrofitFactory
import com.oliviermarteaux.shared.retrofit.WebAppContainer
import com.oliviermarteaux.shared.utils.AndroidLogger
import com.oliviermarteaux.shared.utils.CLOTHES_API_URL

class JoiefullApplication: Application() {

    lateinit var container: AppContainer<Item>

    private val apiService: ItemApiService by lazy {
        RetrofitFactory.createFromUrl(CLOTHES_API_URL).create(ItemApiService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        val apiService = apiService
        container = WebAppContainer(
            log = AndroidLogger,
            apiServiceGetData = apiService::getItems,
            mapper = {itemDto: ItemDto -> itemDto.toDomain()},
        )
    }
}