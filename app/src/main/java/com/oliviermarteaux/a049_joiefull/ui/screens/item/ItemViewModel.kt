package com.oliviermarteaux.a049_joiefull.ui.screens.item

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import com.oliviermarteaux.localshared.data.DataRepository
import com.oliviermarteaux.shared.extensions.toLocalCurrencyString
import com.oliviermarteaux.utils.USER_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import kotlin.math.round
import android.net.Uri
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

class ItemViewModel(
    private val repository: DataRepository<Item>,
    savedStateHandle: SavedStateHandle,
//    explicitItemId: Int? = null
) : ViewModel() {

    private val onePaneItemId: Int? = savedStateHandle[ItemDestination.ITEM_ID]
    var twoPaneItemId by mutableStateOf<Int>(1)

    var item by mutableStateOf(Item())

    fun loadItem(itemId: Int) {
        twoPaneItemId = itemId
        viewModelScope.launch {
            item = repository.getItemById(itemId)
        }
    }

    init{
        viewModelScope.launch {
            item = repository.getItemById(onePaneItemId?:twoPaneItemId)
        }
    }

//    init {
//    // Use cached list from repository
//    itemId?.let{item = repository.getItemById(itemId)}
//    }

    /** Tracks whether the item is marked as favorite. */
//    var isFavorite by mutableStateOf(item.reviews.find { it.user == USER_NAME }?.like ?: false)
//        private set
//    val isFavorite: Boolean
//        get() = item.reviews.find { it.user == USER_NAME }?.like ?: false
    /**
     * Toggles the favorite state locally.
     */
    fun toggleFavorite(isFavorite: Boolean) {
//        isFavorite = !isFavorite
        item.reviews.find{it.user == USER_NAME}?.let {
            item = item.copy(
                likes = if (isFavorite) {item.likes + 1} else { item.likes - 1 },
                reviews = item.reviews.map {
                    if (it.user == USER_NAME) {
                        it.copy(like = !it.like)
                    } else {
                        it
                    }
                }
            )
        }?:let{
            item = item.copy(
                likes = item.likes + 1,
                reviews = item.reviews + ItemReview(
                    user = USER_NAME,
                    comment = "",
                    rating = 0,
                    like = true
                )
            )
        }
        viewModelScope.launch { item = repository.updateItem(item) }
    }

    /** Tracks the user item rating */
//    var rating by mutableIntStateOf(item.reviews.find { it.user == USER_NAME }?.rating ?: 0)
//        private set
//    val rating: Int
//        get() = item.reviews.find { it.user == USER_NAME }?.rating ?: 0

    fun updateRating(newRating: Int) {
//        rating = newRating
        item.reviews.find{it.user == USER_NAME}?.let {
            item = item.copy(
                reviews = item.reviews.map {
                    if (it.user == USER_NAME) {
                        it.copy(rating = newRating)
                    } else {
                        it
                    }
                }
            )
        }?:let{
            item = item.copy(
                reviews = item.reviews + ItemReview(
                    user = USER_NAME,
                    comment = "",
                    rating = newRating,
                    like = false
                )
            )
        }
//        if (item.id >= 0) {
            viewModelScope.launch { item = repository.updateItem(item) }
//        }
    }

    /** Tracks the user item comment */
    var comment by mutableStateOf(item.reviews.find { it.user == USER_NAME }?.comment ?: "")
        private set

    fun updateComment(newComment: String) {
        comment = newComment
        item.reviews.find{it.user == USER_NAME}?.let {
            item = item.copy(
                reviews = item.reviews.map {
                    if (it.user == USER_NAME) {
                        it.copy(comment = newComment)
                    } else {
                        it
                    }
                }
            )
        }?:let{
            item = item.copy(
                reviews = item.reviews + ItemReview(
                    user = USER_NAME,
                    comment = newComment,
                    rating = 0,
                    like = false
                )
            )
        }
        viewModelScope.launch { repository.updateItem(item) }
    }

    fun rating(item: Item): Double = round(item.reviews.map { it.rating }.filter { it != 0 }.average() *10)/10

    fun shareArticle(context: Context) {
        viewModelScope.launch {
            //info: Download image into cache
            val imageFile = withContext(Dispatchers.IO) {
                val connection = URL(item.picture.url).openStream()
                val file = File(context.cacheDir, "shared_image.jpg")
                FileOutputStream(file).use { output ->
                    connection.copyTo(output)
                }
                file
            }

            //info:  Create URI via FileProvider
            val imageUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile
            )

            //info:  Message personnalisé que l'utilisateur peut éditer avant envoi
            val shareText = """
                
            Have a look at this item !
            
            ${item.name}
            
            ${item.description}
            
            Current price: ${item.price.toLocalCurrencyString()}
            
            My review:
            ${item.reviews.find { it.user == USER_NAME }?.comment ?: ""}
            
        """.trimIndent()+"\n"

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            //info:  Affiche le menu des apps sociales (Facebook, Twitter, etc.)
            context.startActivity(
                Intent.createChooser(intent, "Sharing via")
            )
        }
    }
}