package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.SpamNumberDAO
import com.example.myapplication.data.entity.SpamNumberEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpamRepositoryImplTest {

    private lateinit var spamNumberDao: SpamNumberDAO
    private lateinit var repository: SpamRepositoryImpl

    @Before
    fun setup() {
        spamNumberDao = mockk()
        repository = SpamRepositoryImpl(spamNumberDao)
    }

    @Test
    fun `getSpamNumberByPhoneNumber devolve o valor do dao`() = runTest {
        val entity = SpamNumberEntity(
            phoneNumber = "918876579",
            reportCount = 10,
            category = "Fraud"
        )

        coEvery { spamNumberDao.getSpamNumberByPhoneNumber("918876579") } returns entity

        val result = repository.getSpamNumberByPhoneNumber("918876579")

        assertEquals(entity, result)
    }

    @Test
    fun `getSpamNumberByPhoneNumber devolve null quando dao devolve null`() = runTest {
        coEvery { spamNumberDao.getSpamNumberByPhoneNumber("912733636") } returns null

        val result = repository.getSpamNumberByPhoneNumber("912733636")

        assertNull(result)
    }

    @Test
    fun `isSpamNumber devolve o valor do dao`() = runTest {
        coEvery { spamNumberDao.isSpamNumber("918876579") } returns true

        val result = repository.isSpamNumber("918876579")

        assertTrue(result)
    }

    @Test
    fun `insertSpamNumbers chama insertAll no dao`() = runTest {
        val spamNumbers = listOf(
            SpamNumberEntity("918876579", 10, "Fraud"),
            SpamNumberEntity("960144155", 5, "Spam")
        )

        coEvery { spamNumberDao.insertAll(spamNumbers) } returns Unit

        repository.insertSpamNumbers(spamNumbers)

        coVerify(exactly = 1) {
            spamNumberDao.insertAll(spamNumbers)
        }
    }

    @Test
    fun `getAllSpamNumbers devolve lista do dao`() = runTest {
        val spamNumbers = listOf(
            SpamNumberEntity("918876579", 10, "Fraud"),
            SpamNumberEntity("960144155", 5, "Spam")
        )

        coEvery { spamNumberDao.getAllSpamNumbers() } returns spamNumbers

        val result = repository.getAllSpamNumbers()

        assertEquals(2, result.size)
        assertEquals("918876579", result[0].phoneNumber)
        assertEquals("960144155", result[1].phoneNumber)
    }
}