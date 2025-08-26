package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.R.attr.rating
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.shared.composables.VitesseIcon
import com.oliviermarteaux.shared.composables.VitesseImage
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.utils.Logger
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.floor
import kotlin.math.round

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
fun ItemsList(
    items: List<Item>,
) {
    Text("Items")
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items){
//            val log: Logger = koinInject() // inject Logger from Koin
//            log.d(it.picture.url)
//            VitesseImage(photoUri = it.picture.url)
//            Text(it.name)
            ItemCard(it)
        }
    }
}

@Composable
fun ItemCard(
    item: Item
){
    Column(
        modifier = Modifier
    ){
        Box(modifier = Modifier){
            VitesseImage(
                photoUri = item.picture.url,
            )
            Card(){
                Row(){
                    VitesseIcon(
                        icon = Icons.Outlined.FavoriteBorder,
                    )
                    Text(text = item.likes.toString())
                }
            }
        }
        Row(){
            Text(text = item.name)
            Text(text = rating(item).toString())
        }
        Row(){
            Text(item.price.toLocalCurrencyString())
            if (item.originalPrice != item.price) {Text(
                text = item.originalPrice.toLocalCurrencyString(),
                style = TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            ) }
        }
    }
}

fun rating(item: Item): Double = round(item.reviews.map { it.rating }.average()*10)/10

@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    ItemCard(
        Item(
            id = 0,
            picture = ItemPicture(
                url = "android.resource://com.oliviermarteaux.a049_joiefull/drawable/martyna_siddeswara.jpg",
                description = "Sac à main orange posé sur une poignée de porte"
            ),
            name = "Sac à main orange",
            category = ItemCategory.ACCESSORIES,
            likes = 56,
            price = 69.99,
            originalPrice = 69.99,
            description = "Sac à main orange posé sur une poignée de porte",
            reviews = emptyList()
        )
    )
}