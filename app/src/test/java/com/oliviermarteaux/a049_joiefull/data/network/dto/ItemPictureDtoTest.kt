package com.oliviermarteaux.a049_joiefull.data.network.dto

import org.junit.Assert.assertNotNull
import org.junit.Test

class ItemPictureDtoTest {
    @Test
    fun itemPictureDto_CompanionSerializer_NotNull() {
        val serializer = ItemPictureDto.serializer()
        assertNotNull(serializer)
    }
}