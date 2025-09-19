package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemReviewDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemReviewDtoMapperTest {

    @Test
    fun itemReviewDtoToDomain_withAllFieldsNonNull_mapsCorrectly() {
        // Arrange
        val dto = ItemReviewDto(
            user = "Alice",
            comment = "Nice product",
            rating = 5,
            like = true
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals("Alice", domain.user)
        assertEquals("Nice product", domain.comment)
        assertEquals(5, domain.rating)
        assertEquals(true, domain.like)
    }

    @Test
    fun itemReviewDtoToDomain_withNullFields_mapsToDefaultValues() {
        // Arrange
        val dto = ItemReviewDto(
            user = null,
            comment = null,
            rating = null,
            like = null
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals("", domain.user)
        assertEquals("", domain.comment)
        assertEquals(0, domain.rating)
        assertEquals(false, domain.like)
    }

    @Test
    fun itemReviewDtoToDomain_withPartialNullFields_appliesDefaultsOnlyToNulls() {
        // Arrange
        val dto = ItemReviewDto(
            user = "Bob",
            comment = null,
            rating = 3,
            like = null
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals("Bob", domain.user)
        assertEquals("", domain.comment) // default applied
        assertEquals(3, domain.rating)   // kept from dto
        assertEquals(false, domain.like) // default applied
    }
}
