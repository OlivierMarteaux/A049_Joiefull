package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.data.repository.WebDataRepository
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import kotlinx.coroutines.launch

class ItemViewModel(
    private val repository: DataRepository<Item>,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDestination.ITEM_ID])

    var uiState by mutableStateOf<Item?>(null)
        private set

    init {
        // Use cached list from repository
        uiState = repository.getItemById(itemId)
    }
}