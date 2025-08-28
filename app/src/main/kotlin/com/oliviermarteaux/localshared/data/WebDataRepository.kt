package com.oliviermarteaux.localshared.data

import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.shared.utils.Logger

class WebDataRepository<T, D>(
    private val apiServiceGetData: suspend () -> List<D>,
    private val mapper: (D) -> T,
    private val log: Logger
) : DataRepository<T> {

    private var cache: List<T>? = null

    override suspend fun getData(): Result<List<T>> = try {
        log.d("WebDataRepository: Api call successful")
        cache = apiServiceGetData().map(mapper)
        Result.success(cache!!)
//        Result.success(apiServiceGetData().map(mapper))
    } catch (e: Exception) {
        log.d("WebDataRepository: Api call failed. Reason: ${e.message}")
        Result.failure(e)
    }
}