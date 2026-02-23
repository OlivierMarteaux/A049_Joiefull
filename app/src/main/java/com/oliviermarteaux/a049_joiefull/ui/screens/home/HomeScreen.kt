package com.oliviermarteaux.a049_joiefull.ui.screens.home

import android.R.attr.text
import android.R.attr.top
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemScreen
import com.oliviermarteaux.a049_joiefull.ui.screens.item.PaymentUiState
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.SharedIconToggle
import com.oliviermarteaux.shared.composables.texts.TextBodyMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleLarge
import com.oliviermarteaux.shared.composables.texts.TextTitleSmall
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
    payUiState: PaymentUiState = PaymentUiState.NotStarted,
    onGooglePayButtonClick: () -> Unit,
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = SharedPadding.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val cdProgressIndicator = stringResource(R.string.cd_progress_indicator)
        when (val state = uiState) {
            is UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.clearAndSetSemantics{
                    contentDescription = cdProgressIndicator
                }
            )
            is UiState.Error -> {
                TextTitleLarge(
                    text = stringResource(R.string.error),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = viewModel::loadItems,
                    modifier = Modifier.padding(top = SharedPadding.xxl),
                    colors = ButtonDefaults.buttonColors()
                ){
                    TextTitleLarge(
                        text = stringResource(R.string.retry),
                        textAlign = TextAlign.Center
                    )
                }
            }
            is UiState.Empty -> TextTitleLarge(
                text = stringResource(R.string.empty),
                textAlign = TextAlign.Center
            )
            is UiState.Success -> Row(horizontalArrangement = Arrangement.SpaceEvenly){
                HomeItemsList(
                    items = state.data,
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
                        payUiState = payUiState,
                        onGooglePayButtonClick = onGooglePayButtonClick
                    )
                }
            }
        }
    }
}

@Composable
fun HomeItemsList(
    items: List<Item>,
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
            .semantics {
                collectionInfo = CollectionInfo(
                    rowCount = categories.size,
                    columnCount = 1
                )
            }
    ){

        categories.forEachIndexed { index, it ->
            val categoryItems = items.filter { item -> item.category == it }
            val focusRequester = remember { FocusRequester() }
            val context = LocalContext.current
            val categoryTitle = it.getTitle(context)
            val cdCategory: String =
                stringResource(
                    R.string.cd_category,
                    categoryTitle,
                    categoryItems.size
                )
            val cdBrowseItem: String = stringResource(R.string.browse_items)
            HomeLazyRow(
                category = it,
                items = categoryItems,
                toggleFavorite = toggleFavorite,
                rating = rating,
                selectItem = selectItem,
                focusRequester = focusRequester,
                categoryTitle = categoryTitle,
                modifier = Modifier.semantics {
                    contentDescription = cdCategory
                    onClick (label = cdBrowseItem) {
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
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
    selectItem: (Int) -> Unit,
    focusRequester : FocusRequester,
    categoryTitle: String,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
    ){
        TextTitleLarge(
            text = categoryTitle,
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
                val cdItem0: String = stringResource(R.string.cd_item_0, categoryTitle)
                val cdItem1 : String =
                    stringResource(R.string.cd_item_1, item.name, item.likes, rating(item))
                val cdItem2: String =
                    if (item.originalPrice != item.price) {
                        stringResource(R.string.cd_item_2) + item.price.toLocalCurrencyString()+". "
                    } else {
                        item.originalPrice.toLocalCurrencyString()+". "
                    }
                val cdItem3: String =
                    if(item.reviews.find{it.user == USER_NAME}?.like == true) {
                        stringResource(R.string.cd_item_3)
                    } else {""}

                val cdItem: String = cdItem0 + cdItem1 + cdItem2 + cdItem3

                ItemCard(
                    item = item,
                    toggleFavorite = toggleFavorite,
                    rating = rating,
                    modifier = Modifier
                        .focusRequester((if (index == 0) focusRequester else remember { FocusRequester() }))
                        .focusable()
                        .semantics {
                            contentDescription = cdItem
                            collectionItemInfo = CollectionItemInfo(0, 1, index, 1)
                        }
                        .combinedClickable(
                            onClickLabel = stringResource(R.string.open_item),
                            onClick = { selectItem(item.id) },
                            onLongClickLabel = stringResource(R.string.like_item),
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
    toggleFavorite: (Int, Boolean) -> Unit,
    rating: (Item) -> Double,
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

        Box(modifier = Modifier.clearAndSetSemantics{}){
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
                        contentDescription = item.likes.toString() + stringResource(R.string.likes),
                        modifier = Modifier
                            .fontScaledSize()
                            .semantics(mergeDescendants = true) {}
                    )
                    Spacer(Modifier.size(SharedPadding.small))
                    TextTitleSmall(
                        text = item.likes.toString(),
                        modifier = Modifier.clearAndSetSemantics{}
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