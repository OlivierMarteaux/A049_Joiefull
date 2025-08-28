package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.R.attr.rating
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleLarge
import com.oliviermarteaux.shared.composables.text.TextTitleMedium
import com.oliviermarteaux.shared.composables.text.TextTitleSmall
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.extensions.fontScaledWidth
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.ui.theme.SharedSize
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.round

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_screen
}

@Composable
fun HomeScreen(
    navigateToItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(SharedPadding.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text("Error")
                is UiState.Empty -> Text("Empty")
                is UiState.Success -> HomeItemsList(
                    items = state.data,
                    navigateToItem = navigateToItem
                )
            }
        }
    }
}

@Composable
fun HomeItemsList(
    items: List<Item>,
    navigateToItem: (Int) -> Unit
) {
    HomeLazyRow(category = ItemCategory.TOPS, items = items, navigateToItem = navigateToItem)
    HomeLazyRow(category = ItemCategory.BOTTOMS, items = items, navigateToItem = navigateToItem)
    HomeLazyRow(category = ItemCategory.SHOES, items = items, navigateToItem = navigateToItem)
    HomeLazyRow(category = ItemCategory.ACCESSORIES, items = items, navigateToItem = navigateToItem)
}

@Composable
fun HomeLazyRow(
    category: ItemCategory,
    items: List<Item>,
    navigateToItem: (Int) -> Unit
){
    TextTitleLarge(text = category.name, modifier = Modifier.fillMaxWidth())
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SharedPadding.medium,
                bottom = SharedPadding.extraLarge
            ),
        horizontalArrangement= Arrangement.spacedBy(SharedPadding.medium)
    ) {
        items(items.filter { it.category == category }) {
            ItemCard(
                item = it,
                onClick = navigateToItem
            )
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onClick: (Int) -> Unit
){
    val imageSize = 198.dp
    val fontSize = MaterialTheme.typography.titleSmall.fontSize
    Log.d("OM_TAG", fontSize.toString())
    val fontSizeDp = with(LocalDensity.current) { fontSize.toDp() }
    Log.d("OM_TAG", fontSizeDp.toString())
    Log.d("OM_TAG", "image size = $imageSize")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.width(with(LocalDensity.current) { max((fontSize).toDp()*17.4f, imageSize) })
        /**
        fontSize: 14.sp --> 11.34.dp in xxs font
        imageSize: 198.dp
        scale = 198 / 11.34 = 17.4f
        */
        modifier = Modifier
            .fontScaledWidth(fontSize = fontSize, scale = 17.4f, min = imageSize)
            .clickable(onClick = { onClick(item.id) })
    ){
        Box(modifier = Modifier){
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(SharedShapes.xxl),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
            )
            Card(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SharedPadding.large)
                .clip(SharedShapes.xxl)
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
                        modifier = Modifier.fontScaledSize()
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
                    modifier = Modifier.fontScaledSize(),
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