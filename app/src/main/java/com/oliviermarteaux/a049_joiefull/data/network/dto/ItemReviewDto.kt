package com.oliviermarteaux.a049_joiefull.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemReviewDto(
    val user: String?,
    val comment: String?,
    val rating: Int?,
    val like: Boolean?
)