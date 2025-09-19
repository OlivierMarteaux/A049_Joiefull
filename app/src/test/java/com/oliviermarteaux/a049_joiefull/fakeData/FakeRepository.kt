package com.oliviermarteaux.a049_joiefull.fakeData

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository : DataRepository<Item> {

    override suspend fun getDataStream(): Result<Flow<List<Item>>> =
        Result.success(flowOf(fakeItemList))

    override suspend fun getItemById(id: Int): Item =
        fakeItemList.first { it.id == id }

    override fun getItemByIdStream(id: Int): Flow<Item> =
        flowOf(fakeItemList.first { it.id == id })

    override suspend fun updateItem(item: Item): Item = item
}