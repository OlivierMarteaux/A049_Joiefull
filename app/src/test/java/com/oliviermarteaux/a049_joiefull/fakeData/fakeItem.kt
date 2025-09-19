package com.oliviermarteaux.a049_joiefull.fakeData

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture

val fakeItem : Item =
    Item(
        id = 0,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            description = "Sac à main orange posé sur une poignée de porte"
        ),
        name = "Sac à main orange",
        category = ItemCategory.ACCESSORIES,
        likes = 56,
        price = 69.99,
        originalPrice = 69.99,
        description = "Sac à main orange posé sur une poignée de porte.",
        reviews = emptyList()
    )