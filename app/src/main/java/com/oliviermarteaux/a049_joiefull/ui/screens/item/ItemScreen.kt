package com.oliviermarteaux.a049_joiefull.ui.screens.item

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.google.pay.button.PayButton
import com.oliviermarteaux.a049_joiefull.R
import com.oliviermarteaux.a049_joiefull.ui.navigation.NavigationDestination
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.SharedIconButton
import com.oliviermarteaux.shared.composables.SharedIconToggle
import com.oliviermarteaux.shared.composables.SharedImage
import com.oliviermarteaux.shared.composables.SharedRatingBar
import com.oliviermarteaux.shared.composables.texts.TextBodyLarge
import com.oliviermarteaux.shared.composables.texts.TextBodyMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleMedium
import com.oliviermarteaux.shared.extensions.fontScaledSize
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.shared.ui.semanticsContentDescription
import com.oliviermarteaux.shared.ui.theme.SharedColor
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedShapes
import com.oliviermarteaux.shared.utils.SharedContentType
import com.oliviermarteaux.utils.USER_NAME
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
    contentType: SharedContentType = SharedContentType.LIST_ONLY,
    payUiState: PaymentUiState = PaymentUiState.NotStarted,
    onGooglePayButtonClick: () -> Unit,
) {
    if(contentType == SharedContentType.LIST_AND_DETAIL){ viewModel.loadItem(itemId) }
    val item = viewModel.item
    val context = LocalContext.current

    val cdItem1 : String =
        stringResource(R.string.cd_item_1, item.name, item.likes, viewModel.rating(item))
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

    val cdItem: String = cdItem1 + cdItem2 + cdItem3

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SharedPadding.extraLarge)
            .semantics {
                collectionInfo = CollectionInfo(
                    rowCount = 4,
                    columnCount = 1
                )
            }
    ) {
        //info: 1st talkback item: item picture and data
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = SharedPadding.xxl)
                .semantics {
                    contentDescription = cdItem
                    collectionItemInfo = CollectionItemInfo(0, 1, 0, 1)
                }
        ) {
            SharedAsyncImage(
                photoUri = item.picture.url,
                modifier = Modifier
                    .aspectRatio(451 / 408f)
                    .fillMaxWidth()
                    .clip(SharedShapes.xxl)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
            )
            val cdBackButton: String = stringResource(R.string.back_button)
            val cdBackAction: String = stringResource(R.string.go_back_to_home_screen)
            if (contentType == SharedContentType.LIST_ONLY) {
                SharedIconButton(
                    icon = Icons.AutoMirrored.Outlined.ArrowBack,
                    tint = Color.Black,
                    onClick = { navigateBack(itemId) },
                    modifier = Modifier
                        .padding(SharedPadding.extraSmall)
                        .align(Alignment.TopStart)
                        .clearAndSetSemantics {
                            contentDescription = semanticsContentDescription(
                                onClickLabel = cdBackAction,
                                contentDescription = cdBackButton
                            )
                        }
                )
            }
            val cdShareButton: String = stringResource(R.string.share_button)
            val cdShareAction: String = stringResource(R.string.share_this_item_on_social_networks)
            SharedIconButton(
                icon = Icons.Outlined.Share,
                tint = Color.Black,
                onClick = { viewModel.shareArticle(context) },
                modifier = Modifier
                    .padding(SharedPadding.extraSmall)
                    .align(Alignment.TopEnd)
                    .clearAndSetSemantics {
                        contentDescription = semanticsContentDescription(
                            onClickLabel = cdShareAction,
                            contentDescription = cdShareButton
                        )
                    }
            )
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(SharedPadding.large)
                    .clip(SharedShapes.xxl)
                    .semantics(mergeDescendants = true) {}
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
                    val cdLikeCheckBox: String = stringResource(R.string.like_checkbox)
                    val cdLikeAction: String = stringResource(R.string.add_or_remove_a_like_on_this_item)
                    val cdItemLiked: String = stringResource(R.string.item_is_liked)
                    val cdItemNotLiked: String = stringResource(R.string.item_is_not_liked)
                    SharedIconToggle(
                        iconChecked = Icons.Filled.Favorite,
                        iconUnchecked = Icons.Outlined.FavoriteBorder,
                        checked = viewModel.item.reviews.find{it.user == USER_NAME }?.like?:false,
                        onCheckedChange = { viewModel.toggleFavorite(it) },
                        modifier = Modifier
                            .fontScaledSize()
                            .clearAndSetSemantics{
                                stateDescription =
                                    if (viewModel.item.reviews.find { it.user == USER_NAME }?.like
                                            ?: false
                                    ) cdItemLiked else cdItemNotLiked
                                contentDescription = semanticsContentDescription(
                                    contentDescription = cdLikeCheckBox,
                                    onClickLabel = cdLikeAction,
                                )
                            }
                    )
                    Spacer(Modifier.size(SharedPadding.small))
                    TextTitleMedium(
                        text = item.likes.toString(),
                        modifier = Modifier.clearAndSetSemantics{}
                    )
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
                .clearAndSetSemantics{}
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
                .clearAndSetSemantics{}
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
        val cdDetailedItemDescription = stringResource(R.string.detailed_description, item.description)
        //info: 2nd talkback item: item description
        TextBodyMedium(
            text = item.description,
            modifier = Modifier
                .padding(bottom = SharedPadding.xxl)
                .semantics {
                    contentDescription = cdDetailedItemDescription
                    collectionItemInfo = CollectionItemInfo(0, 1, 1, 1)
                }
        )
        //info: 3rd talkback item: item rating
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = SharedPadding.extraLarge)
                .semantics(mergeDescendants = true) {
                    collectionItemInfo = CollectionItemInfo(0, 1, 2, 1)
                }
        ){
            SharedImage(
                painter = painterResource(id = R.drawable.martyna_siddeswara),
                modifier = Modifier
                    .padding(end = SharedPadding.extraLarge)
                    .size(48.dp)
                    .clip(CircleShape)
            )
            val cdStarRatingBar: String = stringResource(R.string.cd_star_rating_bar)
            val cdRatingAction: String = stringResource(R.string.cdRatingAction)
            val cdRatingStateNo: String = stringResource(R.string.cdRatingStateNo)
            val cdRatingXstars: String = stringResource(R.string.cd_rating_xstars, item.reviews.find { it.user == USER_NAME }?.rating ?: 0)
            SharedRatingBar(
                iconChecked = Icons.Filled.Star,
                iconUnchecked = ImageVector.vectorResource(R.drawable.star_24dp),
                stars = 5,
                rating = item.reviews.find{it.user == USER_NAME }?.rating?:0,
                modifier = Modifier
                    .padding(end = SharedPadding.medium)
                    .clearAndSetSemantics{
                        contentDescription = cdStarRatingBar
                        onClick(
                            label = cdRatingAction,
                            action = {
                                viewModel.updateRating(
                                    ((item.reviews.find { it.user == USER_NAME }?.rating
                                        ?: 0) % 5) + 1
                                )
                                return@onClick true
                            })
                        stateDescription =
                            when (item.reviews.find { it.user == USER_NAME }?.rating ?: 0) {
                                0 -> cdRatingStateNo
                                else -> cdRatingXstars
                            }
                    },
                tint = SharedColor.Orange,
                iconModifier = Modifier.fontScaledSize(scale = 2f),
                onRatingChanged = { viewModel.updateRating(it) }
            )
        }

        var newComment by remember { mutableStateOf(viewModel.comment) }
        LaunchedEffect(item.id) { newComment = viewModel.comment }

        val focusManager = LocalFocusManager.current
        val cdShareReviewFalse: String = stringResource(R.string.cd_share_review_false)
        val cdComment = semanticsContentDescription(
            state = newComment.isNotEmpty(),
            trueStateDescription = stringResource(R.string.cd_share_true),
            falseStateDescription = cdShareReviewFalse,
            onClickLabel = stringResource(R.string.cd_comment_action),
            text = newComment,
        )

        //info: 4th talkback item: user item review
        OutlinedTextField(
            value = newComment,
            onValueChange = { newComment = it ; viewModel.updateComment(it)},
            modifier = Modifier
                .padding(bottom = SharedPadding.extraLarge)
                .fillMaxWidth()
                .clearAndSetSemantics{
                    contentDescription = cdComment
                    collectionItemInfo = CollectionItemInfo(0, 1, 3, 1)
                },
            label = { TextBodyMedium(text = cdShareReviewFalse) },
            shape = SharedShapes.large,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        if (payUiState !is PaymentUiState.NotStarted) {
            PayButton(
                modifier = Modifier
                    .testTag("payButton")
                    .fillMaxWidth(),
                onClick = onGooglePayButtonClick,
                allowedPaymentMethods = PaymentsUtil.allowedPaymentMethods.toString()
            )
        }
    }
}