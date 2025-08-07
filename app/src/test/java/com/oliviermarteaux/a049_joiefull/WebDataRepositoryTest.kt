package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemList
import com.oliviermarteaux.shared.data.WebDataRepository
import com.oliviermarteaux.shared.utils.NoOpLogger
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WebDataRepositoryTest {

    private lateinit var itemApiService: ItemApiService
    private lateinit var webItemRepository: WebDataRepository<Item, ItemDto>

    @Before
    fun setup(){
        itemApiService = mock(ItemApiService::class.java)
        webItemRepository = WebDataRepository(
            apiServiceGetData = itemApiService::getItems,
            mapper = {itemDto -> itemDto.toDomain()},
            log = NoOpLogger
        )
    }

    @Test
    fun webDataRepository_GetItemsSuccessfullyFromApi_ReturnsListOfItems()= runTest {
        `when`(itemApiService.getItems()).thenReturn(fakeItemDtoList)
        val result: Result<List<Item>> = webItemRepository.getData()
        verify(itemApiService).getItems()
        assert(result == Result.success(fakeItemList))
    }

    @Test
    fun webDataRepository_GetItemsFailsFromApi_ReturnsFailure() = runTest {
        `when`(itemApiService.getItems()).thenThrow(RuntimeException("API call failed"))
        val result = webItemRepository.getData()
        verify(itemApiService).getItems()
        assert(result.isFailure)
    }
}