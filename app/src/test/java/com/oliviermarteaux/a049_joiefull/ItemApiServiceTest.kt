//package com.oliviermarteaux.a049_joiefull
//
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
//import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
//import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
//import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
//import com.oliviermarteaux.a049_joiefull.fakeData.fakeJson
//import kotlinx.coroutines.test.runTest
//import kotlinx.serialization.json.Json
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNotNull
//import org.junit.Before
//import org.junit.Test
//import retrofit2.Retrofit
//
//class ItemApiServiceTest {
//
//    private lateinit var mockWebServer: MockWebServer
//    private lateinit var itemApiService: ItemApiService
//
//    @Before
//    fun setup(){
//        mockWebServer = MockWebServer()
//        mockWebServer.start()
//
//        val contentType = "application/json".toMediaType()
//        val json = Json{ignoreUnknownKeys = true}
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(mockWebServer.url("/"))
//            .addConverterFactory(json.asConverterFactory(contentType))
//            .build()
//
//        itemApiService = retrofit.create(ItemApiService::class.java)
//    }
//
//    @After
//    fun tearDown(){
//        mockWebServer.shutdown()
//    }
//
//    @Test
//    fun itemApi_getItems_returnsCorrectItems() = runTest {
//        mockWebServer.enqueue(MockResponse().setBody(fakeJson).setResponseCode(200))
//
//        val items: List<ItemDto> = itemApiService.getItems()
//
//        assertEquals(items.size, 12)
//
//        for (i in items.indices) {
//            assertEquals(items[i].name, fakeItemDtoList[i].name)
//            assertEquals(items[i].category, fakeItemDtoList[i].category)
//            assertEquals(items[i].picture?.url, fakeItemDtoList[i].picture?.url)
//            assertEquals(items[i].picture?.description, fakeItemDtoList[i].picture?.description)
//        }
//    }
//
//    @Test
//    fun itemPictureDto_CompanionSerializer_NotNull() {
//        val serializer = ItemPictureDto.serializer()
//        assertNotNull(serializer)
//    }
//}