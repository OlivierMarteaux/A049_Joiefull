package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class ItemPictureDtoMapperTest {

    @Test
    fun itemPictureDtoMapper_NotNullFieldsItemPictureDtoToDomain_shouldMapCorrectly() {

        for (dto in fakeItemDtoList) {
            val domain = dto.picture?.toDomain()
            assertEquals(dto.picture?.url, domain?.url)
            assertEquals(dto.picture?.description, domain?.description)
        }
    }

    @Test
    fun itemPictureDtoMapper_NullFieldsItemPictureDtoToDomain_shouldMapCorrectly() {
        val dto = ItemPictureDto(url = null, description = null)
        val domain = dto.toDomain()

        assertEquals("", domain.url)
        assertEquals("", domain.description)
    }
}