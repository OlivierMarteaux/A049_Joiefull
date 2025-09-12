package com.oliviermarteaux.a049_joiefull.domain.model

data class ItemReview(
    val user: String = "",
    val comment: String = "",
    val rating: Int = 0,
    val like: Boolean = false
)