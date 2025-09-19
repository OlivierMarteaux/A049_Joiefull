package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.domain.model.Item
import com.oliviermarteaux.a049_joiefull.domain.model.ItemCategory
import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import com.oliviermarteaux.a049_joiefull.domain.model.ItemReview
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemMapperTest {

    @Test
    fun itemToDto_withValidItem_mapsAllFieldsCorrectly() {
        // Arrange
        val item = Item(
            id = 1,
            picture = ItemPicture(
                url = "https://example.com/pic.jpg",
                description = "A sample picture"
            ),
            name = "Sample Item",
            category = ItemCategory.TOPS, // example enum
            likes = 42,
            price = 19.99,
            originalPrice = 29.99,
            description = "A nice test item",
            reviews = listOf(
                ItemReview(
                    user = "Alice",
                    comment = "Great!",
                    rating = 5,
                    like = true
                ),
                ItemReview(
                    user = "Bob",
                    comment = "Not bad",
                    rating = 3,
                    like = false
                )
            )
        )

        // Act
        val dto = item.toDto()

        // Assert
        assertEquals(1, dto.id)
        assertEquals("https://example.com/pic.jpg", dto.picture?.url)
        assertEquals("A sample picture", dto.picture?.description)
        assertEquals("Sample Item", dto.name)
        assertEquals("TOPS", dto.category) // enum converted to String
        assertEquals(42, dto.likes)
        assertEquals(19.99, dto.price?:0.0, 0.0)
        assertEquals(29.99, dto.originalPrice?:0.0, 0.0)
        assertEquals("A nice test item", dto.description)

        // Reviews
        assertEquals(2, dto.reviews?.size)
        assertEquals("Alice", dto.reviews?.get(0)?.user)
        assertEquals("Great!", dto.reviews?.get(0)?.comment)
        assertEquals(5, dto.reviews?.get(0)?.rating)
        assertEquals(true, dto.reviews?.get(0)?.like)

        assertEquals("Bob", dto.reviews?.get(1)?.user)
        assertEquals("Not bad", dto.reviews?.get(1)?.comment)
        assertEquals(3, dto.reviews?.get(1)?.rating)
        assertEquals(false, dto.reviews?.get(1)?.like)
    }

    @Test
    fun itemToDto_withEmptyReviews_mapsEmptyList() {
        // Arrange
        val item = Item(
            id = 2,
            picture = ItemPicture(
                url = "https://example.com/empty.jpg",
                description = "Empty picture"
            ),
            name = "Empty Reviews Item",
            category = ItemCategory.ACCESSORIES,
            likes = 0,
            price = 0.0,
            originalPrice = 0.0,
            description = "",
            reviews = emptyList()
        )

        // Act
        val dto = item.toDto()

        // Assert
        assertEquals(2, dto.id)
        assertEquals("Empty Reviews Item", dto.name)
        assertEquals("ACCESSORIES", dto.category)
        assertEquals(0, dto.reviews?.size)
    }
}
