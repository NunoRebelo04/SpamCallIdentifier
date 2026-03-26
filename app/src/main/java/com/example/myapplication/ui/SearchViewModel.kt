package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.SearchHistoryEntity
import com.example.myapplication.data.repository.HistoryRepository
import com.example.myapplication.data.repository.SpamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val spamRepository: SpamRepository,
    private val historyRepository: HistoryRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    init{
        observeHistory()
        loadSpamNumbers()
    }

    fun onPhoneNumberChanged(phoneNumber: String){
        _uiState.value=_uiState.value.copy(
            phoneNumber= phoneNumber,
            errorMessage=null
        )
    }

    fun searchNumber(){
        var number= _uiState.value.phoneNumber.trim()

        if(number.isBlank()){
            _uiState.value=_uiState.value.copy(
                errorMessage = "Introduza um número."
            )
            return
        }

        if(!isValidPhoneNumber(number)){
            _uiState.value=_uiState.value.copy(
                errorMessage = "Número de telefone inválido."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            val result = spamRepository.getSpamNumberByPhoneNumber(number)
            val isSpam= result!=null

            historyRepository.insertSearch(
                SearchHistoryEntity(
                    phoneNumber = number,
                    isSpam = isSpam
                )
            )

            _uiState.value=_uiState.value.copy(
                isLoading = false,
                searchResult = result,
                isSpam = isSpam
            )
        }
    }

    fun onHistoryTabSelected(tab : HistoryTab){
        _uiState.value=_uiState.value.copy(
            selectedHistoryTab = tab
        )
    }

    private fun observeHistory(){
        viewModelScope.launch{
            combine(
                historyRepository.getAllHistorySearch(),
                historyRepository.getAllHistoryBlockedCalls()
            ) { searchHistory, blockedCalls ->
                _uiState.value.copy(
                    searchHistory = searchHistory,
                    blockedCalls = blockedCalls
                )
            }.collect { updatedState->
                _uiState.value=updatedState
            }
        }
    }

    private fun loadSpamNumbers(){
        viewModelScope.launch{
            val spamNumbers= spamRepository.getAllSpamNumbers()
            _uiState.value=_uiState.value.copy(
                spamNumbers=spamNumbers
            )
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String) : Boolean{
        return phoneNumber.all {it.isDigit()} && phoneNumber.length == 9
    }
}