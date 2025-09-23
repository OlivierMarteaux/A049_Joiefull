package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import org.junit.Assert
import org.junit.Test

class ItemDtoMapperTest {

    @Test
    fun itemDtoMapper_NotNullFieldsItemDtoToDomain_shouldMapCorrectly() {
        val dtoList = fakeItemDtoList

        for (dto in dtoList) {
            val domain = dto.toDomain()
            Assert.assertEquals(dto.id, domain.id)
            Assert.assertEquals(dto.picture?.url, domain.picture.url)
            Assert.assertEquals(dto.picture?.description, domain.picture.description)
            Assert.assertEquals(dto.name, domain.name)
            Assert.assertEquals(dto.category, domain.category.name)
            Assert.assertEquals(dto.likes, domain.likes)
            Assert.assertEquals(dto.price, domain.price)
            Assert.assertEquals(dto.originalPrice, domain.originalPrice)
        }
    }

    @Test
    fun itemDtoMapper_NullFieldsItemDtoToDomain_shouldMapCorrectly() {
        val dto = ItemDto(
            id = 42,
            picture = null,
            name = null,
            category = null,
            likes = null,
            price = null,
            originalPrice = null,
            description = null,
            reviews = null
        )

        val domain = dto.toDomain()

        Assert.assertEquals(42, domain.id)
        Assert.assertEquals("", domain.picture.url)
        Assert.assertEquals("", domain.picture.description)
        Assert.assertEquals("Unnamed", domain.name)
        Assert.assertEquals(ItemCategory.ACCESSORIES, domain.category)
        Assert.assertEquals(0, domain.likes)
        Assert.assertEquals(0.0, domain.price, 0.01)
        Assert.assertEquals(0.0, domain.originalPrice, 0.01)
    }

    @Test
    fun itemDtoMapper_InvalidCategoryItemDtoToDomain_shouldMapCorrectly() {
        val dto = ItemDto(
            id = 3,
            picture = null,
            name = "Invalid Category Item",
            category = "invalid-category",
            likes = 5,
            price = 10.0,
            originalPrice = 15.0,
            description = null,
            reviews = null
        )

        val domain = dto.toDomain()

        Assert.assertEquals(ItemCategory.ACCESSORIES, domain.category)
    }
}