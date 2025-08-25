package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture

fun ItemDto.toDomain(): Item {
    return Item(
        id = id,
        picture = picture?.toDomain()
            ?: ItemPicture(
                url = "",
                description = ""
            ),
        name = name ?: "Unnamed",
        category = category?.let {
            runCatching {
                ItemCategory.valueOf(
                    it.uppercase()
                )
            }.getOrElse { ItemCategory.ACCESSORIES }
        }
            ?: ItemCategory.ACCESSORIES,
        likes = likes ?: 0,
        price = price ?: 0.0,
        originalPrice = originalPrice ?: 0.0
    )
}