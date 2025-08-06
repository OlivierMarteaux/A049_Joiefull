package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.repository.WebItemRepository
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemList
import com.oliviermarteaux.a049_joiefull.fakeData.fakeJson
import org.junit.Before
import com.oliviermarteaux.shared.utils.NoOpLogger
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WebItemRepositoryTest {

    private lateinit var itemApiService: ItemApiService
    private lateinit var webItemRepository: WebItemRepository

    @Before
    fun setup(){
        itemApiService = mock(ItemApiService::class.java)
        webItemRepository = WebItemRepository(
            itemApiService = itemApiService,
            log = NoOpLogger
        )
    }

    @Test
    fun webItemRepository_GetItemsSuccessfullyFromApi_ReturnsListOfItems()= runTest {
        `when`(itemApiService.getItems()).thenReturn(fakeItemList)
        val result = webItemRepository.getItems()

        verify(itemApiService).getItems()
        assert(result == Result.success(fakeItemList))
    }

    @Test
    fun webItemRepository_GetItemsFailsFromApi_ReturnsFailure() = runTest {
        `when`(itemApiService.getItems()).thenThrow(RuntimeException("API call failed"))
        val result = webItemRepository.getItems()
        verify(itemApiService).getItems()
        assert(result.isFailure)
    }
}