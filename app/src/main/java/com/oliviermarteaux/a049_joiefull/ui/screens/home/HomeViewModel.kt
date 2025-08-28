package com.oliviermarteaux.a049_joiefull.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.ui.UiState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: DataRepository<Item>
) : ViewModel() {

    // Exposed state for the UI
    var uiState: UiState<Item> by mutableStateOf(UiState.Loading)
        private set

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
}