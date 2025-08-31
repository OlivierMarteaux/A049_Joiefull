package com.oliviermarteaux.a049_joiefull.ui.screens.item

import android.R.attr.label
import android.R.attr.rating
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.oliviermarteaux.shared.composables.sharedRatingBar
import com.oliviermarteaux.shared.composables.text.TextBodyLarge
import com.oliviermarteaux.shared.composables.text.TextBodyMedium
import com.oliviermarteaux.shared.composables.text.TextTitleMedium
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.utils.SharedContentType
import org.koin.compose.viewmodel.koinViewModel

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
    contentType: SharedContentType = SharedContentType.LIST_ONLY
) {
    if(contentType == SharedContentType.LIST_AND_DETAIL){ viewModel.loadItem(itemId) }
    val item = viewModel.item
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(SharedPadding.extraLarge),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = SharedPadding.xxl)
            ) {
                SharedAsyncImage(
                    photoUri = item.picture.url,
                    modifier = Modifier
                        .size(400.dp)
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
                            checked = viewModel.isFavorite,
                            onCheckedChange = { viewModel.toggleFavorite() },
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
                viewModel.updateRating(
                    sharedRatingBar(
                        iconChecked = Icons.Filled.Star,
                        iconUnchecked = ImageVector.vectorResource(R.drawable.star_24dp),
                        stars = 5,
                        rating = viewModel.rating,
                        modifier = Modifier.padding(end = SharedPadding.medium),
                        tint = SharedColor.Orange,
                        iconModifier = Modifier.fontScaledSize(scale = 2f)
                    )
                )
            }
            OutlinedTextField(
                value = viewModel.comment,
                onValueChange = { viewModel.updateComment(it)},
                modifier = Modifier
                    .padding(bottom = SharedPadding.extraLarge)
                    .fillMaxWidth(),
                label = { TextBodyMedium(text = "Share your experience on this item here") },
                shape = SharedShapes.large
            )
        }
    }
}