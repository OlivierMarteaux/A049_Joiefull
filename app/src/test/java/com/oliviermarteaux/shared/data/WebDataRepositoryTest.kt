package com.oliviermarteaux.shared.data

import com.oliviermarteaux.shared.utils.NoOpLogger
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WebDataRepositoryTest {

    @Before
    fun setup() {
    }

    @Test
    fun `getData should return mapped data on success`() = runBlocking {
        // Given
        val dtoList = listOf("1", "2", "3") // D
        val expectedDomainList = listOf(1, 2, 3) // T

        val apiServiceGetData: suspend () -> List<String> = { dtoList }
        val mapper: (String) -> Int = { it.toInt() }

        val repository = WebDataRepository(
            apiServiceGetData = apiServiceGetData,
            mapper = mapper,
            log = NoOpLogger
        )

        // When
        val result = repository.getData()

        // Then
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(expectedDomainList, result.getOrNull())
    }

    @Test
    fun `getData should return failure on exception`() = runBlocking {
        // Given
        val exception = RuntimeException("Network error")
        val apiServiceGetData: suspend () -> List<String> = { throw exception }
        val mapper: (String) -> Int = { it.toInt() }

        val repository = WebDataRepository(
            apiServiceGetData = apiServiceGetData,
            mapper = mapper,
            log = NoOpLogger
        )

        // When
        val result = repository.getData()

        // Then
        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(exception, result.exceptionOrNull())
    }
}