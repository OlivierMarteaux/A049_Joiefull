package com.oliviermarteaux.a049_joiefull.data.network.dto

import org.junit.Assert.assertNotNull
import org.junit.Test

class ItemReviewDtoTest {
    @Test
    fun itemReviewDto_CompanionSerializer_NotNull() {
        val serializer = ItemReviewDto.serializer()
        assertNotNull(serializer)
    }
}