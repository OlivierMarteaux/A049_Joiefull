package com.oliviermarteaux.a049_joiefull.domain.model

data class Item(
    val id: Int,
    val picture: ItemPicture,
    val name: String,
    val category: ItemCategory,
    val likes: Int,
    val price: Double,
    val originalPrice: Double
)
