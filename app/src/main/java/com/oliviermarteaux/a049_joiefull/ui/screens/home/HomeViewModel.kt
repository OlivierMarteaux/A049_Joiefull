package com.oliviermarteaux.a049_joiefull.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.utils.USER_NAME
import kotlinx.coroutines.launch
import kotlin.math.round

class HomeViewModel(
    private val repository: DataRepository<Item>
) : ViewModel() {

    var uiState: UiState<Item> by mutableStateOf(UiState.Loading)
        private set

    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        if (uiState is UiState.Success) {
            val successState = (uiState as UiState.Success<Item>)
            val items = successState.data
            val item = items.firstOrNull { it.id == id } ?: return

            val updatedItem = item.reviews.find { it.user == USER_NAME }?.let {
                // Toggle like if review already exists
                item.copy(
                    likes = if (isFavorite) item.likes + 1 else item.likes - 1,
                    reviews = item.reviews.map { review ->
                        if (review.user == USER_NAME) {
                            review.copy(like = !review.like)
                        } else review
                    }
                )
            } ?: run {
                // Create new review if not exists
                item.copy(
                    likes = item.likes + 1,
                    reviews = item.reviews + ItemReview(
                        user = USER_NAME,
                        comment = "",
                        rating = 0,
                        like = true
                    )
                )
            }

            // Replace the old item in the list with the updated one
            val updatedList = items.map { if (it.id == id) updatedItem else it }

            // Update UI state immutably
            uiState = UiState.Success(updatedList)

            // Update backend
            viewModelScope.launch {
                repository.updateItem(updatedItem)
            }
        }
    }

    init {
        loadItems()
    }

    /**
     * Loads Items from the repository and updates the UI state.
     */
    fun loadItems() {
        viewModelScope.launch {
            uiState = UiState.Loading
            repository.getData().fold(
                onSuccess = {
                    uiState =
                        if (it.isEmpty()) { UiState.Empty }
                        else { UiState.Success(it) }
                },
                onFailure = { uiState = UiState.Error }
            )
        }
    }

    fun rating(item: Item): Double = round(item.reviews.map { it.rating }.filter { it != 0 }.average() *10)/10
}