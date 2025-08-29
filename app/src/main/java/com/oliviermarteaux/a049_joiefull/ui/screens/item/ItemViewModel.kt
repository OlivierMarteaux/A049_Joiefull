package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.data.repository.WebDataRepository
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.utils.USER_NAME
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class ItemViewModel(
    private val repository: DataRepository<Item>,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDestination.ITEM_ID])

//    var item by mutableStateOf<Item?>(null)
//        private set

    // ✅ directly initialized, so never null
    var item by mutableStateOf(repository.getItemById(itemId))
        private set

    /** Tracks whether the item is marked as favorite. */
    var isFavorite by mutableStateOf(false)
        private set
    /**
     * Toggles the favorite state locally.
     */
    fun toggleFavorite() {
        isFavorite = !isFavorite
        item.reviews.find{it.user == USER_NAME}?.let {
            item = item.copy(
                likes = if (isFavorite) {item.likes + 1} else { item.likes - 1 },
                reviews = item.reviews.map {
                    if (it.user == USER_NAME) {
                        it.copy(like = !it.like)
                    } else {
                        it
                    }
                }
            )
        }?:let{
            item = item.copy(
                likes = item.likes + 1,
                reviews = item.reviews + ItemReview(
                    user = USER_NAME,
                    comment = "",
                    rating = 0,
                    like = true
                )
            )
        }
        viewModelScope.launch { item = repository.updateItem(item) }
    }

//    init {
//        // Use cached list from repository
//        item = repository.getItemById(itemId)
//    }
}