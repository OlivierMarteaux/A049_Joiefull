package com.oliviermarteaux.a049_joiefull.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val picture: ItemPicture,
    val name: String,
    val category: ItemCategory,
    val likes: Int,
    val price: Double,
    @SerialName("original_price") val originalPrice: Double
)

@Serializable
data class ItemPicture(
    val url: String,
    val description: String
)

@Serializable
enum class ItemCategory {
    ACCESSORIES, BOTTOMS, SHOES, TOPS
}