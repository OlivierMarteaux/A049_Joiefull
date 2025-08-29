package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.a049_joiefull.ui.screens.home.rating
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
import com.oliviermarteaux.shared.ui.theme.SharedSize
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
    viewModel: ItemViewModel = koinViewModel()
) {
    val item = viewModel.item

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(SharedPadding.extraLarge),
        ) {
            Box(modifier = Modifier.padding(bottom = SharedPadding.xxl)) {
                SharedAsyncImage(
                    photoUri = item.picture.url,
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth()
                        .clip(SharedShapes.xxl),
                    contentScale = ContentScale.Crop,
                )
                SharedIconButton(
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                    tint = Color.Black,
                    onClick = { navigateBack(itemId) },
                    modifier = Modifier.padding(SharedPadding.extraSmall).align(Alignment.TopStart)
                )
                SharedIconButton(
                    icon = Icons.Outlined.Share,
                    tint = Color.Black,
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(SharedPadding.extraSmall).align(Alignment.TopEnd)
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
                        Spacer(Modifier.size(SharedSize.small))
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
                    Spacer(Modifier.size(SharedSize.small))
                    TextBodyLarge(text = rating(item).toString())
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
                    modifier = Modifier.padding(end = SharedPadding.extraLarge).size(48.dp).clip(CircleShape)
                )
                SharedRatingBar()
            }
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.padding(bottom = SharedPadding.extraLarge).fillMaxWidth(),
                label = { TextBodyMedium(text = "Share your experience on this item") },
                shape = SharedShapes.large
            )
        }
    }
}