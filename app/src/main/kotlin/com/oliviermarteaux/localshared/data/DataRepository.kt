package com.oliviermarteaux.localshared.data

import kotlinx.coroutines.flow.Flow

interface DataRepository<T> {

    suspend fun getData(): Result<List<T>>

    suspend fun getItemById(id: Int): T

    suspend fun updateItem(item: T): T

    suspend fun getDataStream(): Result<Flow<List<T>>>

}