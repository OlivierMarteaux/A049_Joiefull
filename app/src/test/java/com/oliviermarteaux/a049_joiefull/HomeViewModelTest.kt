package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemList
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeViewModel
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: DataRepository<Item>
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk<DataRepository<Item>>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should be Loading initially`() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(listOf<Item>()))
        viewModel = HomeViewModel(repository)

        assertTrue(viewModel.uiState is UiState.Loading)
    }

    @Test
    fun `uiState should be Success when repository returns items`() = runTest {
        val items = fakeItemList

        coEvery { repository.getDataStream() } returns Result.success(flowOf(items))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        val state = viewModel.uiState
        assertTrue(state is UiState.Success)
        assertEquals(items, (state as UiState.Success).data)
    }

    @Test
    fun `uiState should be Empty when repository returns empty list`() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(emptyList()))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        assertTrue(viewModel.uiState is UiState.Empty)
    }

    @Test
    fun `uiState should be Error when repository fails`() = runTest {
        coEvery { repository.getDataStream() } returns Result.failure(Exception("Failed"))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        assertTrue(viewModel.uiState is UiState.Error)
    }

    @Test
    fun `selectItem should update selectedItemId`() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(emptyList()))
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectItem(42)
        assertEquals(42, viewModel.selectedItemId)
    }

    @Test
    fun `toggleFavorite should update likes and add review when none exists`() = runTest {
        val itemList = fakeItemList

        coEvery { repository.getDataStream() } returns Result.success(flowOf(itemList))
        coEvery { repository.updateItem(any()) } returns itemList.first()

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        viewModel.toggleFavorite(1, true)
        val state = viewModel.uiState as UiState.Success
        val updatedItem = state.data.first()

        assertEquals(1, updatedItem.likes)
        assertEquals(1, updatedItem.reviews.size)
        assertTrue(updatedItem.reviews.first().like)
        coVerify { repository.updateItem(updatedItem) }
    }
}
