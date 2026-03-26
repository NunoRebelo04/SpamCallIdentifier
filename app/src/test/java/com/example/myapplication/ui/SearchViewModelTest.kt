package com.example.myapplication.ui

import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import com.example.myapplication.data.entity.SpamNumberEntity
import com.example.myapplication.data.repository.HistoryRepository
import com.example.myapplication.data.repository.SpamRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var spamRepository: SpamRepository
    private lateinit var historyRepository: HistoryRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        spamRepository = mockk()
        historyRepository = mockk()

        every { historyRepository.getAllHistorySearch() } returns flowOf(emptyList())
        every { historyRepository.getAllHistoryBlockedCalls() } returns flowOf(emptyList())
        coEvery { spamRepository.getAllSpamNumbers() } returns emptyList()

        viewModel = SearchViewModel(
            spamRepository = spamRepository,
            historyRepository = historyRepository
        )
    }

    @Test
    fun `onPhoneNumberChanged atualiza o estado`() {
        viewModel.onPhoneNumberChanged("918876579")

        assertEquals("918876579", viewModel.uiState.value.phoneNumber)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `searchNumber com numero vazio mostra erro`() {
        viewModel.onPhoneNumberChanged("")
        viewModel.searchNumber()

        assertEquals("Número de telefone inválido.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `searchNumber com numero invalido mostra erro`() {
        viewModel.onPhoneNumberChanged("123")
        viewModel.searchNumber()

        assertEquals("Número de telefone inválido.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `searchNumber com numero spam atualiza resultado e guarda historico`() = runTest {
        val spamNumber = SpamNumberEntity(
            phoneNumber = "918876579",
            reportCount = 10,
            category = "Fraud"
        )

        coEvery { spamRepository.getSpamNumberByPhoneNumber("918876579") } returns spamNumber
        coEvery { historyRepository.insertSearch(any()) } returns Unit

        viewModel.onPhoneNumberChanged("918876579")
        viewModel.searchNumber()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSpam == true)
        assertEquals(spamNumber, viewModel.uiState.value.searchResult)
        assertNull(viewModel.uiState.value.errorMessage)

        coVerify(exactly = 1) {
            historyRepository.insertSearch(
                match<SearchHistoryEntity> {
                    it.phoneNumber == "918876579" && it.isSpam
                }
            )
        }
    }

    @Test
    fun `searchNumber com numero nao spam atualiza resultado e guarda historico`() = runTest {
        coEvery { spamRepository.getSpamNumberByPhoneNumber("912733636") } returns null
        coEvery { historyRepository.insertSearch(any()) } returns Unit

        viewModel.onPhoneNumberChanged("912733636")
        viewModel.searchNumber()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isSpam == true)
        assertNull(viewModel.uiState.value.searchResult)
        assertNull(viewModel.uiState.value.errorMessage)

        coVerify(exactly = 1) {
            historyRepository.insertSearch(
                match<SearchHistoryEntity> {
                    it.phoneNumber == "912733636" && !it.isSpam
                }
            )
        }
    }

    @Test
    fun `onHistoryTabSelected altera a tab selecionada`() {
        viewModel.onHistoryTabSelected(HistoryTab.BLOCKED_CALLS)

        assertEquals(
            HistoryTab.BLOCKED_CALLS,
            viewModel.uiState.value.selectedHistoryTab
        )
    }

    @Test
    fun `init carrega numeros spam para o estado`() = runTest {
        val spamNumbers = listOf(
            SpamNumberEntity("918876579", 10, "Fraud"),
            SpamNumberEntity("960144155", 5, "Spam")
        )

        every { historyRepository.getAllHistorySearch() } returns flowOf(emptyList())
        every { historyRepository.getAllHistoryBlockedCalls() } returns flowOf(emptyList())
        coEvery { spamRepository.getAllSpamNumbers() } returns spamNumbers

        val vm = SearchViewModel(
            spamRepository = spamRepository,
            historyRepository = historyRepository
        )

        advanceUntilIdle()

        assertEquals(2, vm.uiState.value.spamNumbers.size)
        assertEquals("918876579", vm.uiState.value.spamNumbers[0].phoneNumber)
    }

    @Test
    fun `observeHistory atualiza o estado com historico e chamadas bloqueadas`() = runTest {
        val searchHistory = listOf(
            SearchHistoryEntity(
                id = 1,
                phoneNumber = "918876579",
                isSpam = true
            )
        )

        val blockedCalls = listOf(
            BlockedCallEntity(
                id = 1,
                phoneNumber = "918876579",
                category = "Fraud"
            )
        )

        every { historyRepository.getAllHistorySearch() } returns flowOf(searchHistory)
        every { historyRepository.getAllHistoryBlockedCalls() } returns flowOf(blockedCalls)
        coEvery { spamRepository.getAllSpamNumbers() } returns emptyList()

        val vm = SearchViewModel(
            spamRepository = spamRepository,
            historyRepository = historyRepository
        )

        advanceUntilIdle()

        assertEquals(1, vm.uiState.value.searchHistory.size)
        assertEquals(1, vm.uiState.value.blockedCalls.size)
        assertEquals("918876579", vm.uiState.value.searchHistory.first().phoneNumber)
        assertEquals("918876579", vm.uiState.value.blockedCalls.first().phoneNumber)
    }
}