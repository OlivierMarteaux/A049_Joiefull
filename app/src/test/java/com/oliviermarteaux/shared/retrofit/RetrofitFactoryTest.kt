package com.oliviermarteaux.shared.retrofit

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

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
        mockWebServer.enqueue(MockResponse().setBody(response))
        val baseUrl = mockWebServer.url("/").toString()

        val retrofit = RetrofitFactory.createFromUrl(baseUrl)
        val service = retrofit.create(TestApiService::class.java)

        // When
        val result = service.getTest()

        // Then
        assertEquals(response, result)
    }
}