package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.domain.model.ItemPicture
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemPictureMapperTest {

    @Test
    fun itemPictureToDto_withValidItemPicture_mapsAllFieldsCorrectly() {
        // Arrange
        val picture = ItemPicture(
            url = "https://example.com/pic.jpg",
            description = "Sample picture description"
        )

        // Act
        val dto = picture.toDto()

        // Assert
        assertEquals("https://example.com/pic.jpg", dto.url)
        assertEquals("Sample picture description", dto.description)
    }

    @Test
    fun itemPictureToDto_withEmptyFields_mapsEmptyStrings() {
        // Arrange
        val picture = ItemPicture(
            url = "",
            description = ""
        )

        // Act
        val dto = picture.toDto()

        // Assert
        assertEquals("", dto.url)
        assertEquals("", dto.description)
    }
}
