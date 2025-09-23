package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.lifecycle.SavedStateHandle
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItem
import com.oliviermarteaux.shared.data.DataRepository
import com.oliviermarteaux.utils.USER_NAME
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemViewModelTest {

    private lateinit var repository: DataRepository<Item>
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ItemViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mockk()
        savedStateHandle = SavedStateHandle(mapOf(ItemDestination.ITEM_ID to 0))
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun itemViewModel_init_withSavedState_loadsItem() = runTest {

        coEvery { repository.getItemByIdStream(0) } returns flow { emit(fakeItem) }

        viewModel = ItemViewModel(repository, savedStateHandle)
        advanceUntilIdle() // ensures the flow is collected

        assertEquals(fakeItem, viewModel.item)
    }

    @Test
    fun loadItem_withItemId_updatesItemAndTwoPaneItemId() = runTest {
        coEvery { repository.getItemByIdStream(0) } returns flow { emit(fakeItem) }
        viewModel = ItemViewModel(repository, savedStateHandle)

        viewModel.loadItem(0)
        advanceUntilIdle() // ensures the flow is collected

        assertEquals(0, viewModel.twoPaneItemId)
        assertEquals(fakeItem, viewModel.item)
    }

    @Test
    fun toggleFavorite_whenNoExistingReview_addsReviewAndIncrementsLikes() = runTest {
        coEvery { repository.updateItem(any()) } returns fakeItem.copy(likes = 1, reviews = listOf(
            ItemReview(user = USER_NAME, comment = "", rating = 0, like = true)
        ))
        coEvery { repository.getItemByIdStream(any()) } returns flow { emit(fakeItem) }

        viewModel = ItemViewModel(repository, savedStateHandle)

        viewModel.toggleFavorite(true)
        advanceUntilIdle() // ensures the flow is collected

        assertEquals(1, viewModel.item.likes)
        assertEquals(1, viewModel.item.reviews.size)
        assertTrue(viewModel.item.reviews.first().like)
        assertTrue(viewModel.onClickTalkback)
        coVerify { repository.updateItem(any()) }
    }

    @Test
    fun updateRating_whenNoExistingReview_addsReviewWithRating() = runTest {
        coEvery { repository.updateItem(any()) } returns fakeItem.copy(reviews = listOf(
            ItemReview(USER_NAME, comment = "", rating = 5, like = false)
        ))
        coEvery { repository.getItemByIdStream(any()) } returns flow { emit(fakeItem) }

        viewModel = ItemViewModel(repository, savedStateHandle)

        viewModel.updateRating(5)
        advanceUntilIdle() // ensures the flow is collected

        val review = viewModel.item.reviews.first()
        assertEquals(USER_NAME, review.user)
        assertEquals(5, review.rating)
        coVerify { repository.updateItem(any()) }
    }

    @Test
    fun updateComment_whenNoExistingReview_addsReviewWithComment() = runTest {
        coEvery { repository.updateItem(any()) } returns fakeItem.copy(reviews = listOf(
            ItemReview(USER_NAME, comment = "Nice", rating = 0, like = false)
        ))
        coEvery { repository.getItemByIdStream(any()) } returns flow { emit(fakeItem) }

        viewModel = ItemViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        viewModel.updateComment("Nice")
        advanceUntilIdle() // ensures the flow is collected

        val review = viewModel.item.reviews.first()
        assertEquals("Nice", review.comment)
        coVerify { repository.updateItem(any()) }
    }
}
