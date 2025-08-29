package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture

fun ItemPicture.toDto(): ItemPictureDto {
    return ItemPictureDto(
        url = url,
        description = description
    )
}