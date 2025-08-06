package com.oliviermarteaux.a049_joiefull

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.oliviermarteaux.a049_joiefull.data.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemList
import com.oliviermarteaux.a049_joiefull.fakeData.fakeJson
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ItemApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var itemApiService: ItemApiService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        val json = Json{ignoreUnknownKeys = true}

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        itemApiService = retrofit.create(ItemApiService::class.java)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun itemApi_getItems_returnsCorrectItems() = runTest {
        mockWebServer.enqueue(MockResponse().setBody(fakeJson).setResponseCode(200))

        val items = itemApiService.getItems()

        assertEquals(items.size, 12)

        assertEquals(items[0].name, fakeItemList[0].name)
        assertEquals(items[0].category, fakeItemList[0].category)
        assertEquals(items[0].picture.url, fakeItemList[0].picture.url)
        assertEquals(items[0].picture.description, fakeItemList[0].picture.description)

        assertEquals(items[5].name, fakeItemList[5].name)
        assertEquals(items[5].category, fakeItemList[5].category)
        assertEquals(items[5].picture.url, fakeItemList[5].picture.url)
        assertEquals(items[5].picture.description, fakeItemList[5].picture.description)

        assertEquals(items[11].name, fakeItemList[11].name)
        assertEquals(items[11].category, fakeItemList[11].category)
        assertEquals(items[11].picture.url, fakeItemList[11].picture.url)
        assertEquals(items[11].picture.description, fakeItemList[11].picture.description)

    }
}