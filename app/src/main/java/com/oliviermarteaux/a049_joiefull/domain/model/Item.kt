package com.oliviermarteaux.a049_joiefull.domain.model

data class Item(
    val id: Int = -1,
    val picture: ItemPicture = ItemPicture(),
    val name: String = "Unnamed",
    val category: ItemCategory = ItemCategory.ACCESSORIES,
    val likes: Int = 0,
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val description: String = "",
    val reviews: List<ItemReview> = emptyList(),
)
