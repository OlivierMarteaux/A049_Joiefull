package com.oliviermarteaux.a049_joiefull.data.network.mapper

import android.R.attr.description
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemReviewDto
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview

fun ItemReviewDto.toDomain(): ItemReview {
    return ItemReview(
        user = user ?: "",
        comment = comment ?: "",
        rating = rating ?: 0.0
    )
}