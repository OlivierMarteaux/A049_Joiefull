package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemReviewDto
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview

fun ItemReview.toDto(): ItemReviewDto {
    return ItemReviewDto(
        user = user,
        comment = comment,
        rating = rating,
        like = like
    )
}