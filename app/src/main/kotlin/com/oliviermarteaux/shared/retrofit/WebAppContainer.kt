package com.oliviermarteaux.shared.retrofit

import com.oliviermarteaux.shared.data.AppContainer
import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger

class WebAppContainer<T,D>(
    private val apiServiceGetData: suspend () -> List<D>,
    private val mapper: (D) -> T,
    private val log: Logger
): AppContainer<T> {

//    private val itemApiService: ItemApiService by lazy {
//        RetrofitFactory.createFromUrl(CLOTHES_API_URL).create(ItemApiService::class.java)
//    }

    override val dataRepository: DataRepository<T> by lazy {
        WebDataRepository(
            apiServiceGetData = apiServiceGetData,//apiService::getData,
            mapper = mapper, //{itemDto -> itemDto.toDomain()},
            log = log
        )
    }
}