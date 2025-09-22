package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.a049_joiefull.data.network.api.ItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.api.KtorItemApiService
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemDto
import com.oliviermarteaux.a049_joiefull.data.network.dto.ItemPictureDto
import com.oliviermarteaux.a049_joiefull.fakeData.fakeItemDtoList
import com.oliviermarteaux.a049_joiefull.fakeData.fakeJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class KtorItemApiServiceTest {
    private lateinit var itemApiService: ItemApiService
    private lateinit var client: HttpClient

    @Before
    fun setup() {
        val json = Json { ignoreUnknownKeys = true }

        client = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
            engine {
                addHandler { request ->
                    // ✅ Match full path used in KtorItemApiService
                    if (request.url.toString().endsWith("clothes")) {
                        respond(
                            content = fakeJson, // JSON test data
                            status = HttpStatusCode.OK,
                            headers = headersOf("Content-Type", "application/json")
                        )
                    } else {
                        respondError(HttpStatusCode.NotFound)
                    }
                }
            }
        }

        itemApiService = KtorItemApiService(client)
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun ktorItemApiService_getItems_returnsCorrectItems() = runTest {
        val items: List<ItemDto> = itemApiService.getItems()

        assertEquals(12, items.size)

        for (i in items.indices) {
            assertEquals(fakeItemDtoList[i].name, items[i].name)
            assertEquals(fakeItemDtoList[i].category, items[i].category)
            assertEquals(fakeItemDtoList[i].picture?.url, items[i].picture?.url)
            assertEquals(fakeItemDtoList[i].picture?.description, items[i].picture?.description)
        }
    }
}