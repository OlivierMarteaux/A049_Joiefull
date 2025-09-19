package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.R.attr.category
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
            is UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.progressSemantics()
            )
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
            .semantics() {
                collectionInfo = CollectionInfo(
                    rowCount = categories.size,
                    columnCount = 1
                )
            }
    ){
        categories.forEachIndexed { index, it ->
            val categoryItems = items.filter { item -> item.category == it }
            val focusRequester = remember { FocusRequester() }
            HomeLazyRow(
                category = it,
                items = categoryItems,
                navigateToItem = navigateToItem,
                toggleFavorite = toggleFavorite,
                rating = rating,
                selectItem = selectItem,
                focusRequester = focusRequester,
                modifier = Modifier.semantics {
                    contentDescription = "${it.title} clothes list. Contains ${categoryItems.size} items."
                    onClick (label = "browse items") {
                        focusRequester.requestFocus()
                    }
                    collectionItemInfo = CollectionItemInfo(index, 1, 0, 1)
                }
            )
        }
    }
}

@Composable
fun HomeLazyRow(
    category: ItemCategory,
    items: List<Item>,
    navigateToItem: (Int) -> Unit,
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
    selectItem: (Int) -> Unit,
    focusRequester : FocusRequester,
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier
    ){
        TextTitleLarge(
            text = category.title,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { hideFromAccessibility() }
        )
        LazyRow(
            contentPadding = PaddingValues(start = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = SharedPadding.medium,
                    bottom = SharedPadding.extraLarge
                )
                .semantics {
                    // exactly one row, N columns = number of children
                    collectionInfo = CollectionInfo(
                        rowCount = 1,
                        columnCount = items.size
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(SharedPadding.medium)
        ) {
            itemsIndexed(
                items = items,
            ) { index, item ->
                Log.d("OM_TAG", "homeScreen:itemsIndexed(): in category ${category.name}, index for item ${item.id} = $index")
                val cdItem =
                    """
                        In ${category.title} category,
                        ${item.name}. ${item.likes} likes. rated ${rating(item)} stars.
                        ${ 
                            if (item.originalPrice != item.price) { 
                                "discounted " + item.price.toLocalCurrencyString() 
                            } else { 
                                item.originalPrice.toLocalCurrencyString() 
                            }
                        }
                        ${
                            if(item.reviews.find{it.user == USER_NAME}?.like == true) {
                                "You liked this item"
                            } else {""}
                        }
                        """.trimIndent()

                ItemCard(
                    item = item,
                    onClick = navigateToItem,
                    toggleFavorite = toggleFavorite,
                    rating = rating,
                    selectItem = selectItem,
                    modifier = Modifier
                        .focusRequester((if (index == 0) focusRequester else remember { FocusRequester() }))
                        .focusable()
                        .semantics {
                            contentDescription = cdItem
                            collectionItemInfo = CollectionItemInfo(0, 1, index, 1)
                        }
                        .combinedClickable(
                            onClickLabel = "open item",
                            onClick = { selectItem(item.id) },
                            onLongClickLabel = "like item",
                            onLongClick = {
                                toggleFavorite(
                                    item.id,
                                    !(item.reviews.find { review -> review.user == USER_NAME }?.like
                                        ?: false)
                                )
                            }
                        )
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
    ){

        Box(modifier = Modifier.clearAndSetSemantics(){}){
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
                        contentDescription = item.likes.toString() + "likes",
                        modifier = Modifier
                            .fontScaledSize()
                            .semantics(mergeDescendants = true) {}
                    )
                    Spacer(Modifier.size(SharedPadding.small))
                    TextTitleSmall(
                        text = item.likes.toString(),
                        modifier = Modifier.clearAndSetSemantics(){}
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(top = SharedPadding.medium)
                .padding(horizontal = SharedPadding.medium)
                .fillMaxWidth()
                .clearAndSetSemantics {},
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
                .fillMaxWidth()
                .clearAndSetSemantics {},
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            TextBodyMedium(item.price.toLocalCurrencyString())
            if (item.originalPrice != item.price) {
                TextBodyMedium(
                    text = item.originalPrice.toLocalCurrencyString(),
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray,
                )
            }
        }
    }
}