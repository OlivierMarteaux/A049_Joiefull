package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import com.oliviermarteaux.shared.di.WebAppContainer
import com.oliviermarteaux.shared.utils.NoOpLogger
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class WebAppContainerTest {

    @Test
    fun `itemRepository should return mapped items from mocked API`() = runTest {

        val mockApiService = mock<ItemApiService> {
            onBlocking { getItems() } doReturn fakeItemDtoList
        }

        val container = WebAppContainer(
//            apiService = mockApiService,
            apiServiceGetData = mockApiService::getItems,
            mapper = { itemDto -> itemDto.toDomain() },
            log = NoOpLogger
        )

        val result = container.dataRepository.getData()

        assertTrue(result.isSuccess)
        assertEquals(fakeItemDtoList.map { it.toDomain() }, result.getOrNull())
    }
}