package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import org.junit.Test
import org.junit.Assert.assertEquals

class ItemDtoMapperTest {

    @Test
    fun itemDtoMapper_NotNullFieldsItemDtoToDomain_shouldMapCorrectly() {
        val dtoList = fakeItemDtoList

        for (dto in dtoList) {
            val domain = dto.toDomain()
            assertEquals(dto.id, domain.id)
            assertEquals(dto.picture?.url, domain.picture.url)
            assertEquals(dto.picture?.description, domain.picture.description)
            assertEquals(dto.name, domain.name)
            assertEquals(dto.category, domain.category.name)
            assertEquals(dto.likes, domain.likes)
            assertEquals(dto.price, domain.price)
            assertEquals(dto.originalPrice, domain.originalPrice)
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

        assertEquals(42, domain.id)
        assertEquals("", domain.picture.url)
        assertEquals("", domain.picture.description)
        assertEquals("Unnamed", domain.name)
        assertEquals(ItemCategory.ACCESSORIES, domain.category)
        assertEquals(0, domain.likes)
        assertEquals(0.0, domain.price, 0.01)
        assertEquals(0.0, domain.originalPrice, 0.01)
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

        assertEquals(ItemCategory.ACCESSORIES, domain.category)
    }
}