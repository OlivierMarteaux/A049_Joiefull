package com.oliviermarteaux.a049_joiefull.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oliviermarteaux.shared.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.oliviermarteaux.a049_joiefull.domain.model.Item

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text("Error")
            is UiState.Empty -> Text("Empty")
            is UiState.Success -> ItemsList(state.data)
        }
    }
}

@Composable
fun ItemsList(items: List<Item>) {
    Text("Items")
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items){
            Text(it.name)
        }
    }
}