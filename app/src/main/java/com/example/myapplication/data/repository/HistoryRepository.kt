package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun insertSearch(searchHistory: SearchHistoryEntity)
    fun getAllHistorySearch(): Flow<List<SearchHistoryEntity>>

    suspend fun insertBlockedCall(blockedCall: BlockedCallEntity)
    fun getAllHistoryBlockedCalls(): Flow<List<BlockedCallEntity>>
}