package com.oliviermarteaux.a049_joiefull.ui.screens.item

import android.R.attr.category
import android.R.attr.contentDescription
import android.R.attr.label
import android.R.attr.rating
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.SharedIconButton
import com.oliviermarteaux.shared.composables.SharedIconToggle
import com.oliviermarteaux.shared.composables.SharedImage
import com.oliviermarteaux.shared.composables.SharedRatingBar
import com.oliviermarteaux.shared.composables.text.TextBodyLarge
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleMedium
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.utils.SharedContentType
import com.oliviermarteaux.utils.USER_NAME
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import kotlin.math.round

object ItemDestination : NavigationDestination {
    override val route = "item"
    override val titleRes = R.string.item_screen
    const val ITEM_ID = "applicantId"
    val routeWithArgs = "$route/{$ITEM_ID}"
}

@Composable
fun ItemScreen(
    itemId: Int,
    navigateBack: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemViewModel = koinViewModel(),
    contentType: SharedContentType = SharedContentType.LIST_ONLY,
) {
    if(contentType == SharedContentType.LIST_AND_DETAIL){ viewModel.loadItem(itemId) }
    val item = viewModel.item

    val context = LocalContext.current

    val cdItem = """
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
        }"""

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SharedPadding.extraLarge)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = SharedPadding.xxl)
                .clearAndSetSemantics(){
                    contentDescription = cdItem
                }
        ) {
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
                    .aspectRatio(451/408f)
                    .fillMaxWidth()
                    .clip(SharedShapes.xxl)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
            )
            SharedIconButton(
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                tint = Color.Black,
                onClick = { navigateBack(itemId) },
                modifier = Modifier
                    .padding(SharedPadding.extraSmall)
                    .align(Alignment.TopStart)
            )
            SharedIconButton(
                icon = Icons.Outlined.Share,
                tint = Color.Black,
                onClick = { viewModel.shareArticle(context) },
                modifier = Modifier
                    .padding(SharedPadding.extraSmall)
                    .align(Alignment.TopEnd)
            )
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(SharedPadding.large)
                    .clip(SharedShapes.xxl)
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = SharedPadding.medium,
                            vertical = SharedPadding.small
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SharedIconToggle(
                        iconChecked = Icons.Filled.Favorite,
                        iconUnchecked = Icons.Outlined.FavoriteBorder,
                        checked = viewModel.item.reviews.find{it.user == USER_NAME }?.like?:false,
                        onCheckedChange = { viewModel.toggleFavorite(it) },
                        modifier = Modifier.fontScaledSize()
                    )
                    Spacer(Modifier.size(SharedPadding.small))
                    TextTitleMedium(item.likes.toString())
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = SharedPadding.small,
                    end = SharedPadding.medium
                )
        ){
            TextTitleMedium(item.name)
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
                TextBodyLarge(text = viewModel.rating(item).toString())
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = SharedPadding.large,
                    end = SharedPadding.large
                )
        ){
            TextBodyLarge(item.price.toLocalCurrencyString())
            if (item.originalPrice != item.price) {
                TextBodyLarge(
                    text = item.originalPrice.toLocalCurrencyString(),
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            }
        }
        TextBodyMedium(
            text = item.description,
            modifier = Modifier.padding(bottom = SharedPadding.xxl)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = SharedPadding.extraLarge)
        ){
            SharedImage(
                painter = painterResource(id = R.drawable.martyna_siddeswara),
                modifier = Modifier
                    .padding(end = SharedPadding.extraLarge)
                    .size(48.dp)
                    .clip(CircleShape)
            )
            SharedRatingBar(
                iconChecked = Icons.Filled.Star,
                iconUnchecked = ImageVector.vectorResource(R.drawable.star_24dp),
                stars = 5,
                rating = item.reviews.find{it.user == USER_NAME }?.rating?:0,
                modifier = Modifier.padding(end = SharedPadding.medium),
                tint = SharedColor.Orange,
                iconModifier = Modifier.fontScaledSize(scale = 2f),
                onRatingChanged = { viewModel.updateRating(it) }
            )
        }

        var newComment by remember { mutableStateOf(viewModel.comment) }
        LaunchedEffect(item.id) { newComment = viewModel.comment }

        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = newComment,
            onValueChange = { newComment = it ; viewModel.updateComment(it)},
            modifier = Modifier
                .padding(bottom = SharedPadding.extraLarge)
                .fillMaxWidth(),
            label = { TextBodyMedium(text = "Share your experience on this item here") },
            shape = SharedShapes.large,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}

fun rating(item: Item): Double = round(item.reviews.map { it.rating }.filter { it != 0 }.average() *10)/10