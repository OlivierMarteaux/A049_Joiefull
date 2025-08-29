package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.Item

fun Item.toDto(): ItemDto {
    return ItemDto(
        id = id,
        picture = picture.toDto(),
        name = name,
        category = category.name, // enum to string
        likes = likes,
        price = price,
        originalPrice = originalPrice,
        description = description,
        reviews = reviews.map { it.toDto() }
    )
}