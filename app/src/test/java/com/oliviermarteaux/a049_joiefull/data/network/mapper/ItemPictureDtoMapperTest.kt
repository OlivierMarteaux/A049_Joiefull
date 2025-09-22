package com.oliviermarteaux.a049_joiefull.data.network.mapper

import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import org.junit.Assert
import org.junit.Test

class ItemPictureDtoMapperTest {

    @Test
    fun itemPictureDtoMapper_NotNullFieldsItemPictureDtoToDomain_shouldMapCorrectly() {

        for (dto in fakeItemDtoList) {
            val domain = dto.picture?.toDomain()
            Assert.assertEquals(dto.picture?.url, domain?.url)
            Assert.assertEquals(dto.picture?.description, domain?.description)
        }
    }

    @Test
    fun itemPictureDtoMapper_NullFieldsItemPictureDtoToDomain_shouldMapCorrectly() {
        val dto = ItemPictureDto(url = null, description = null)
        val domain = dto.toDomain()

        Assert.assertEquals("", domain.url)
        Assert.assertEquals("", domain.description)
    }
}