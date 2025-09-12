package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.R.attr.category
import android.R.attr.rating
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemScreen
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.SharedIconToggle
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleLarge
import com.oliviermarteaux.shared.composables.text.TextTitleSmall
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.extensions.fontScaledWidth
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.utils.SharedContentType
import com.oliviermarteaux.utils.USER_NAME
import io.ktor.http.headers
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
    viewModel: HomeViewModel,
    contentType: SharedContentType = SharedContentType.LIST_ONLY,
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = SharedPadding.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text("Error")
            is UiState.Empty -> Text("Empty")
            is UiState.Success -> Row(horizontalArrangement = Arrangement.SpaceEvenly){
                HomeItemsList(
                    items = state.data,
                    navigateToItem = navigateToItem,
                    toggleFavorite = viewModel::toggleFavorite,
                    rating = viewModel::rating,
                    modifier = Modifier.weight(734f),
                    selectItem = { id ->
                        viewModel.selectItem(id)
                        if(contentType == SharedContentType.LIST_ONLY) { navigateToItem(id) }
                    }
                )
                AnimatedVisibility(
                    visible = contentType == SharedContentType.LIST_AND_DETAIL,
                    modifier = Modifier.weight(451f)
                ) {
                    ItemScreen(
                        itemId = viewModel.selectedItemId!!,
                        navigateBack = {},
                        contentType = SharedContentType.LIST_AND_DETAIL,
                    )
                }
            }
        }
    }
}

@Composable
fun HomeItemsList(
    items: List<Item>,
    navigateToItem: (Int) -> Unit,
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
    modifier: Modifier = Modifier,
    selectItem: (Int) -> Unit
) {
    val categories = listOf(
        ItemCategory.TOPS,
        ItemCategory.BOTTOMS,
        ItemCategory.SHOES,
        ItemCategory.ACCESSORIES
    )
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState())

    ){
        categories.forEach {
            HomeLazyRow(
                category = it,
                items = items,
                navigateToItem = navigateToItem,
                toggleFavorite = toggleFavorite,
                rating = rating,
                selectItem = selectItem,
                modifier = Modifier.semantics(mergeDescendants = true){}
            )
        }
    }
//    HomeLazyRow(category = ItemCategory.TOPS, items = items, navigateToItem = navigateToItem, toggleFavorite = toggleFavorite, rating = rating)
//    HomeLazyRow(category = ItemCategory.BOTTOMS, items = items, navigateToItem = navigateToItem, toggleFavorite = toggleFavorite, rating = rating)
//    HomeLazyRow(category = ItemCategory.SHOES, items = items, navigateToItem = navigateToItem, toggleFavorite = toggleFavorite, rating = rating)
//    HomeLazyRow(category = ItemCategory.ACCESSORIES, items = items, navigateToItem = navigateToItem, toggleFavorite = toggleFavorite, rating = rating)
}

@Composable
fun HomeLazyRow(
    category: ItemCategory,
    items: List<Item>,
    navigateToItem: (Int) -> Unit,
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
    selectItem: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column (modifier = modifier){
        TextTitleLarge(
            text = category.title,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = SharedPadding.medium,
                    bottom = SharedPadding.extraLarge
                )
                .semantics { hideFromAccessibility() },
            horizontalArrangement = Arrangement.spacedBy(SharedPadding.medium)
        ) {
            items(items.filter { it.category == category }) {
                ItemCard(
                    item = it,
                    onClick = navigateToItem,
                    toggleFavorite = toggleFavorite,
                    rating = rating,
                    selectItem = selectItem,
                    modifier = Modifier.semantics (mergeDescendants = true){ hideFromAccessibility() }
                )
            }
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onClick: (Int) -> Unit,
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
    selectItem: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val imageSize = 198.dp
    val fontSize = MaterialTheme.typography.titleSmall.fontSize
    Log.d("OM_TAG", fontSize.toString())
    val fontSizeDp = with(LocalDensity.current) { fontSize.toDp() }
    Log.d("OM_TAG", fontSizeDp.toString())
    Log.d("OM_TAG", "image size = $imageSize")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        /**
        fontSize: 14.sp --> 11.34.dp in xxs font
        imageSize: 198.dp
        scale = 198 / 11.34 = 17.4f
        */
        modifier = modifier
            .fontScaledWidth(fontSize = fontSize, scale = 17.4f, min = imageSize)
            .clickable(onClick = { /*onClick(item.id);*/ selectItem(item.id) })
    ){
        Box{
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(234 / 256f)
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
                    SharedIconToggle(
                        iconChecked = Icons.Filled.Favorite,
                        iconUnchecked = Icons.Outlined.FavoriteBorder,
                        checked = item.reviews.find{it.user == USER_NAME}?.like ?: false,
                        onCheckedChange = { toggleFavorite(item.id, it) },
                        modifier = Modifier
                            .fontScaledSize()
                            .semantics (mergeDescendants = true){ hideFromAccessibility() }
                    )
                    Spacer(Modifier.size(SharedPadding.small))
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
                Spacer(Modifier.size(SharedPadding.small))
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