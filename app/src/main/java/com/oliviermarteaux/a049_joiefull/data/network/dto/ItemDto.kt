package com.oliviermarteaux.a049_joiefull.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int,
    val picture: ItemPictureDto? = null,
    val name: String? = null,
    val category: String? = null,
    val likes: Int? = null,
    val price: Double? = null,
    @SerialName("original_price") val originalPrice: Double? = null,
    val description: String?,
    val reviews: List<ItemReviewDto>?,
)