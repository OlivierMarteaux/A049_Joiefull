package com.oliviermarteaux.a049_joiefull

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.data.network.mapper.toDomain
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import org.junit.Test
import org.junit.Assert.assertEquals

class ItemPictureDtoMapperTest {

    @Test
    fun `toDomain should map ItemPictureDto correctly when all fields are non-null`() {

        for (dto in fakeItemDtoList) {
            val domain = dto.picture?.toDomain()
            assertEquals(dto.picture?.url, domain?.url)
            assertEquals(dto.picture?.description, domain?.description)
        }
    }

    @Test
    fun `toDomain should map ItemPictureDto with defaults when fields are null`() {
        val dto = ItemPictureDto(url = null, description = null)
        val domain = dto.toDomain()

        assertEquals("", domain.url)
        assertEquals("", domain.description)
    }
}