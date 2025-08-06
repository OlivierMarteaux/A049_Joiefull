package com.oliviermarteaux.a049_joiefull.fakeData

import com.oliviermarteaux.a049_joiefull.data.model.Item
import com.oliviermarteaux.a049_joiefull.data.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.data.model.ItemPicture

val fakeItemList = listOf(
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
        originalPrice = 69.99
    ),
    Item(
        id = 1,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/bottoms/1.jpg",
            description = "Modèle femme qui porte un jean et un haut jaune"
        ),
        name = "Jean pour femme",
        category = ItemCategory.BOTTOMS,
        likes = 55,
        price = 49.99,
        originalPrice = 59.99
    ),
    Item(
        id = 2,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/shoes/1.jpg",
            description = "Modèle femme qui pose dans la rue en bottes de pluie noires"
        ),
        name = "Bottes noires pour l'automne",
        category = ItemCategory.SHOES,
        likes = 4,
        price = 99.99,
        originalPrice = 119.99
    ),
    Item(
        id = 3,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/1.jpg",
            description = "Homme en costume et veste de blazer qui regarde la caméra"
        ),
        name = "Blazer marron",
        category = ItemCategory.TOPS,
        likes = 15,
        price = 79.99,
        originalPrice = 79.99
    ),
    Item(
        id = 4,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/2.jpg",
            description = "Femme dehors qui pose avec un pull en maille vert"
        ),
        name = "Pull vert femme",
        category = ItemCategory.TOPS,
        likes = 15,
        price = 29.99,
        originalPrice = 39.99
    ),
    Item(
        id = 5,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/shoes/2.jpg",
            description = "Escarpins rouges posés sur du marbre"
        ),
        name = "Escarpins de soirée",
        category = ItemCategory.SHOES,
        likes = 15,
        price = 139.99,
        originalPrice = 139.99
    ),
    Item(
        id = 6,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/2.jpg",
            description = "Sac d'aventurier usé accroché dans un arbre en forêt"
        ),
        name = "Sac à dos d'aventurier",
        category = ItemCategory.ACCESSORIES,
        likes = 9,
        price = 69.99,
        originalPrice = 99.99
    ),
    Item(
        id = 7,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/3.jpg",
            description = "Homme jeune stylé en jean et bomber qui pose dans la rue"
        ),
        name = "Bomber automnal pour homme",
        category = ItemCategory.TOPS,
        likes = 30,
        price = 89.99,
        originalPrice = 109.99
    ),
    Item(
        id = 8,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/4.jpg",
            description = "Homme en sweat jaune qui regarde à droite"
        ),
        name = "Sweat jaune",
        category = ItemCategory.TOPS,
        likes = 6,
        price = 39.99,
        originalPrice = 39.99
    ),
    Item(
        id = 9,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/tops/5.jpg",
            description = "T-shirt rose posé sur un cintre dans une penderie"
        ),
        name = "T-shirt casual rose",
        category = ItemCategory.TOPS,
        likes = 35,
        price = 29.99,
        originalPrice = 29.99
    ),
    Item(
        id = 10,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/3.jpg",
            description = "Pendentif rond bleu dans la main d'une femme"
        ),
        name = "Pendentif bleu pour femme",
        category = ItemCategory.ACCESSORIES,
        likes = 70,
        price = 19.99,
        originalPrice = 69.99
    ),
    Item(
        id = 11,
        picture = ItemPicture(
            url = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/bottoms/2.jpg",
            description = "Homme en chemise blanche et pantalon noir assis dans la forêt"
        ),
        name = "Pantalon noir",
        category = ItemCategory.BOTTOMS,
        likes = 54,
        price = 49.99,
        originalPrice = 69.99
    )
)