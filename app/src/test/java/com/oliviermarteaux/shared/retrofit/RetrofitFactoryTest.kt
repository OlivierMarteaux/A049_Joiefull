package com.oliviermarteaux.shared.retrofit

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import kotlin.jvm.java

class RetrofitFactoryTest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `RetrofitFactory returns working Retrofit with correct baseUrl`() = runBlocking {
        // Given
        val response = "Hello, test!"
        mockWebServer.enqueue(MockResponse()
            .setBody("\"$response\"")
            .setHeader("Content-Type", "application/json")
        )
        val baseUrl = mockWebServer.url("/").toString()

        val retrofit = RetrofitFactory.createFromUrl(baseUrl)
        val fakeApiService = retrofit.create(FakeApiService::class.java)

        // When
        val result = fakeApiService.getData()

        // Then
        assertEquals(response, result)
    }
}