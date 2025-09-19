package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItem
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDto
import com.oliviermarteaux.shared.utils.Logger
import com.oliviermarteaux.shared.utils.NoOpLogger
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test

@OptIn(ExperimentalCoroutinesApi::class)
class WebDataRepositoryTest {

    private lateinit var repository: WebDataRepository
    private val apiServiceGetData: suspend () -> List<ItemDto> = mockk()
    private val apiServicePutData: suspend (ItemDto) -> ItemDto = mockk()
    private val apiServiceGetById: suspend (Int) -> ItemDto = mockk()
    private val dtoToDomain: (ItemDto) -> Item = { dto -> fakeItem }
    private val domainToDto: (Item) -> ItemDto = { _ -> fakeItemDto }
    private val log: Logger = NoOpLogger

    @Before
    fun setup() {
        repository = WebDataRepository(
            apiServiceGetData,
            apiServicePutData,
            apiServiceGetById,
            dtoToDomain,
            domainToDto,
            log
        )
    }

    @Test
    fun getDataStream_withSuccess_returnsFlowOfItems() = runTest {
        coEvery { apiServiceGetData.invoke() } returns listOf(fakeItemDto)

        val result = repository.getDataStream()

        assertTrue(result.isSuccess)
        val flow = result.getOrThrow()
        val items = flow.first()
        assertEquals(fakeItem, items.first())
        coVerify { apiServiceGetData.invoke() }
    }

    @Test
    fun getDataStream_withException_returnsFailure() = runTest {
        coEvery { apiServiceGetData.invoke() } throws RuntimeException("Network error")

        val result = repository.getDataStream()

        assertTrue(result.isFailure)
        coVerify { apiServiceGetData.invoke() }
    }

    @Test
    fun getItemById_withValidId_returnsMappedItem() = runTest {
        coEvery { apiServiceGetById.invoke(1) } returns fakeItemDto

        val item = repository.getItemById(1)

        assertEquals(fakeItem, item)
        coVerify { apiServiceGetById.invoke(1) }
    }

    @Test
    fun getItemByIdStream_withExistingItemId_emitsItem() = runTest {
        coEvery { apiServiceGetData.invoke() } returns listOf(fakeItemDto)
        repository.getDataStream() // trigger population

        repository.getItemByIdStream(0).test {
            assertEquals(fakeItem, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateItem_withValidItem_updatesFlowAndReturnsUpdatedItem() = runTest {

        coEvery { apiServicePutData.invoke(any()) } returns fakeItemDto

        // Pre-populate items
        coEvery { apiServiceGetData.invoke() } returns listOf(fakeItemDto)
        repository.getDataStream()

        val result = repository.updateItem(fakeItem)

        assertEquals(fakeItem, result)
        coVerify { apiServicePutData.invoke(fakeItemDto) }
    }

    @Test(expected = RuntimeException::class)
    fun updateItem_withException_throwsError() = runTest {
        coEvery { apiServicePutData.invoke(any()) } throws RuntimeException("Update failed")

        repository.updateItem(fakeItem)
    }
}
