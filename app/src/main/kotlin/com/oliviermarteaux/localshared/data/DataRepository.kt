package com.oliviermarteaux.localshared.data

interface DataRepository<T> {

    suspend fun getData(): Result<List<T>>
    suspend fun getItemById(id: Int): T

    suspend fun updateItem(item: T): T

}