package com.example.myapplication.ui

import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import com.example.myapplication.data.entity.SpamNumberEntity

data class SearchScreenUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val searchResult: SpamNumberEntity? = null,
    val isSpam: Boolean? = null,
    val errorMessage: String? = null,
    val searchHistory: List<SearchHistoryEntity> = emptyList(),
    val blockedCalls: List<BlockedCallEntity> = emptyList(),
    val spamNumbers : List <SpamNumberEntity> = emptyList(),
    val selectedHistoryTab: HistoryTab = HistoryTab.SEARCHES
)