package com.oliviermarteaux.a049_joiefull.ui.screens.item

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.text.TextTitleSmall
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.ui.UiState
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
            Text("ItemScreen")
//            Box(modifier = Modifier) {
//                SharedAsyncImage(
//                    photoUri = item.picture.url,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1f)
//                        .clip(SharedShapes.xxl),
//                    contentScale = ContentScale.Crop,
//                    alignment = Alignment.TopCenter,
//                )
//                Card(
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(SharedPadding.large)
//                        .clip(SharedShapes.xxl)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .padding(
//                                horizontal = SharedPadding.medium,
//                                vertical = SharedPadding.small
//                            ),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically,
//                    ) {
//                        SharedIcon(
//                            icon = Icons.Outlined.FavoriteBorder,
//                            modifier = Modifier.fontScaledSize()
//                        )
//                        Spacer(Modifier.size(SharedSize.small))
//                        TextTitleSmall(item.likes.toString())
//                    }
//                }
//            }
        }
    }
}