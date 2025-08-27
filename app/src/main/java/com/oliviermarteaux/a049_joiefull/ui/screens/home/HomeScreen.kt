package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleMedium
import com.oliviermarteaux.shared.composables.text.TextTitleSmall
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.ui.theme.SharedSize
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(SharedPadding.extraLarge),
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
//    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
    TextTitleMedium(text = "TOPS", modifier = Modifier.fillMaxWidth())
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SharedPadding.medium,
                bottom = SharedPadding.extraLarge
            ),
        horizontalArrangement= Arrangement.spacedBy(SharedPadding.medium)
    ) {
        items(items.filter { it.category == ItemCategory.TOPS }) {
                ItemCard(it)
        }
    }
    TextTitleMedium(text = "BOTTOMS", modifier = Modifier.fillMaxWidth())
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SharedPadding.medium,
                bottom = SharedPadding.extraLarge
            ),
        horizontalArrangement= Arrangement.spacedBy(SharedPadding.medium)
    ) {
        items(items.filter { it.category == ItemCategory.BOTTOMS }) {
            ItemCard(it)
        }
    }
    TextTitleMedium(text = "SHOES", modifier = Modifier.fillMaxWidth())
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SharedPadding.medium,
                bottom = SharedPadding.extraLarge
            ),
        horizontalArrangement= Arrangement.spacedBy(SharedPadding.medium)
    ) {
        items(items.filter { it.category == ItemCategory.SHOES }) {
            ItemCard(it)
        }
    }
    TextTitleMedium(text = "ACCESSORIES", modifier = Modifier.fillMaxWidth())
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SharedPadding.medium,
                bottom = SharedPadding.extraLarge
            ),
        horizontalArrangement= Arrangement.spacedBy(SharedPadding.medium)
    ) {
        items(items.filter { it.category == ItemCategory.ACCESSORIES }) {
            ItemCard(it)
        }
    }
}
//}

@Composable
fun ItemCard(
    item: Item,
){
    val fontSize = MaterialTheme.typography.titleSmall.fontSize
    Log.d("OM_TAG", fontSize.toString())
    val fontSizeDp = with(LocalDensity.current) { fontSize.toDp() }
    Log.d("OM_TAG", fontSizeDp.toString())
    Log.d("OM_TAG", "image size = " + 198.dp.toString())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(with(LocalDensity.current) { max((fontSize).toDp()*17.4f, 198.dp) })
    ){
        Box(modifier = Modifier){
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
//                    .size(198.dp)
                    .size(with(LocalDensity.current) { max((fontSize).toDp()*17.4f, 198.dp) })
                    .clip(SharedShapes.extraLarge),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
            )
            Card(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SharedPadding.large)
            ){
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = SharedPadding.medium,
                            vertical = SharedPadding.small
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    SharedIcon(
                        icon = Icons.Outlined.FavoriteBorder,
                        modifier = Modifier.size(with(LocalDensity.current) { (fontSize * 1.0f).toDp() })
                    )
                    Spacer(Modifier.size(SharedSize.small))
                    TextTitleSmall(item.likes.toString())
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(top = SharedPadding.medium)
                .padding(horizontal = SharedPadding.medium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            TextTitleSmall(text = item.name)
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                SharedIcon(
                    icon = Icons.Filled.Star,
                    modifier = Modifier.size(with(LocalDensity.current) { (fontSize * 1.0f).toDp() }),
                    tint = SharedColor.Orange
                )
                Spacer(Modifier.size(SharedSize.small))
                TextTitleSmall(text = rating(item).toString())
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = SharedPadding.medium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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