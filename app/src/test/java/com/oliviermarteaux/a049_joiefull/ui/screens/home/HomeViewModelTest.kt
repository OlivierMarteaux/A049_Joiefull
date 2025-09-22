package com.oliviermarteaux.a049_joiefull.ui.screens.home

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemList
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
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
    fun homeViewModel_InitWaitingApi_UiStateIsLoading() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(listOf<Item>()))
        viewModel = HomeViewModel(repository)

        Assert.assertTrue(viewModel.uiState is UiState.Loading)
    }

    @Test
    fun homeViewModel_InitApiReached_UiStateIsSuccess() = runTest {
        val items = fakeItemList

        coEvery { repository.getDataStream() } returns Result.success(flowOf(items))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        val state = viewModel.uiState
        Assert.assertTrue(state is UiState.Success)
        Assert.assertEquals(items, (state as UiState.Success).data)
    }

    @Test
    fun homeViewModel_initApiReturnsEmptyList_UiStateIsEmpty() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(emptyList()))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        Assert.assertTrue(viewModel.uiState is UiState.Empty)
    }

    @Test
    fun homeViewModel_initApiFails_UiStateIsError() = runTest {
        coEvery { repository.getDataStream() } returns Result.failure(Exception("Failed"))

        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle() // collect flow

        Assert.assertTrue(viewModel.uiState is UiState.Error)
    }

    @Test
    fun homeViewModel_selectItem_updatesSelectedItemId() = runTest {
        coEvery { repository.getDataStream() } returns Result.success(flowOf(emptyList()))
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectItem(42)
        Assert.assertEquals(42, viewModel.selectedItemId)
    }

    @Test
    fun homeViewModel_toggleFavorite_updatesItemInList() = runTest {
        val itemList = fakeItemList

        coEvery { repository.getDataStream() } returns Result.success(flowOf(itemList))
        coEvery { repository.updateItem(any()) } returns itemList.first()

        viewModel = HomeViewModel(repository)
        advanceUntilIdle() // ensure init block runs and flow is collected

        // Now uiState should be Success
        Assert.assertTrue(viewModel.uiState is UiState.Success)

        viewModel.toggleFavorite(0, true)
        advanceUntilIdle() // ensure updateItem coroutine completes

        val state = viewModel.uiState as UiState.Success
        val updatedItem = state.data.first()

        Assert.assertEquals(57, updatedItem.likes)
        Assert.assertEquals(1, updatedItem.reviews.size)
        Assert.assertTrue(updatedItem.reviews.first().like)

        coVerify {
            repository.updateItem(withArg { updated ->
                Assert.assertEquals(57, updated.likes)
                Assert.assertEquals(1, updated.reviews.size)
                Assert.assertTrue(updated.reviews.first().like)
            })
        }
    }
}