package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemReviewMapperTest {

    @Test
    fun toDto_mapsAllFieldsCorrectly() {
        // Arrange
        val review = ItemReview(
            user = "John Doe",
            comment = "Great product!",
            rating = 5,
            like = true
        )

        // Act
        val dto = review.toDto()

        // Assert
        assertEquals("John Doe", dto.user)
        assertEquals("Great product!", dto.comment)
        assertEquals(5, dto.rating)
        assertEquals(true, dto.like)
    }

    @Test
    fun toDto_mapsEmptyFieldsCorrectly() {
        // Arrange
        val review = ItemReview(
            user = "",
            comment = "",
            rating = 0,
            like = false
        )

        // Act
        val dto = review.toDto()

        // Assert
        assertEquals("", dto.user)
        assertEquals("", dto.comment)
        assertEquals(0, dto.rating)
        assertEquals(false, dto.like)
    }
}
