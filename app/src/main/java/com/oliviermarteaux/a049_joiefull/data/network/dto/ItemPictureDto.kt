package com.oliviermarteaux.a049_joiefull.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemPictureDto(
    val url: String? = null,
    val description: String? = null
)
