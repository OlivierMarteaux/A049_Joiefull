package com.oliviermarteaux.a049_joiefull.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.VitesseIcon
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleSmall
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import org.koin.compose.viewmodel.koinViewModel
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
        modifier = Modifier.width(198.dp)
    ){
        Box(modifier = Modifier){
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
                    .size(198.dp)
                    .clip(SharedShapes.extraLarge),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
            )
            Card(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SharedPadding.large)
                .height(27.dp)
                .width(51.dp)
            ){
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = SharedPadding.medium,
                            vertical = SharedPadding.small
                        )
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    VitesseIcon(
                        icon = Icons.Outlined.FavoriteBorder,
                        modifier = Modifier.size(14.dp)
                    )
                    TextTitleSmall(
                        text = item.likes.toString(),
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextTitleSmall(text = item.name)
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(19.dp)
                    .width(36.dp)
            ){
                SharedIcon(
                    icon = Icons.Filled.Star,
                    modifier = Modifier.size(14.dp),
                    tint = SharedColor.Orange
                )
                TextTitleSmall(text = rating(item).toString())
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextBodyMedium(item.price.toLocalCurrencyString())
            if (item.originalPrice != item.price) {
                TextBodyMedium(
                    text = item.originalPrice.toLocalCurrencyString(),
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            }
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