package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.BlockedCallDAO
import com.example.myapplication.data.dao.SearchHistoryDAO
import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryRepositoryImplTest {

    private lateinit var searchHistoryDao: SearchHistoryDAO
    private lateinit var blockedCallDao: BlockedCallDAO
    private lateinit var repository: HistoryRepositoryImpl

    @Before
    fun setup() {
        searchHistoryDao = mockk()
        blockedCallDao = mockk()
        repository = HistoryRepositoryImpl(searchHistoryDao, blockedCallDao)
    }

    @Test
    fun `insertSearch chama insertSearch no dao`() = runTest {
        val entity = SearchHistoryEntity(
            id = 1,
            phoneNumber = "918876579",
            isSpam = true
        )

        coEvery { searchHistoryDao.insertSearch(entity) } returns Unit

        repository.insertSearch(entity)

        coVerify(exactly = 1) {
            searchHistoryDao.insertSearch(entity)
        }
    }

    @Test
    fun `getAllSearchHistory devolve flow do dao`() = runTest {
        val history = listOf(
            SearchHistoryEntity(
                id = 1,
                phoneNumber = "918876579",
                isSpam = true
            )
        )

        every { searchHistoryDao.getAllSearchHistory() } returns flowOf(history)

        val result = repository.getAllHistorySearch()

        result.collect {
            assertEquals(1, it.size)
            assertEquals("918876579", it.first().phoneNumber)
        }
    }

    @Test
    fun `insertBlockedCall chama insertBlockedCall no dao`() = runTest {
        val entity = BlockedCallEntity(
            id = 1,
            phoneNumber = "918876579",
            category = "Fraud"
        )

        coEvery { blockedCallDao.insertBlockedCall(entity) } returns Unit

        repository.insertBlockedCall(entity)

        coVerify(exactly = 1) {
            blockedCallDao.insertBlockedCall(entity)
        }
    }

    @Test
    fun `getAllBlockedCalls devolve flow do dao`() = runTest {
        val blockedCalls = listOf(
            BlockedCallEntity(
                id = 1,
                phoneNumber = "918876579",
                category = "Fraud"
            )
        )

        every { blockedCallDao.getAllBlockedCalls() } returns flowOf(blockedCalls)

        val result = repository.getAllHistoryBlockedCalls()

        result.collect {
            assertEquals(1, it.size)
            assertEquals("918876579", it.first().phoneNumber)
        }
    }
}