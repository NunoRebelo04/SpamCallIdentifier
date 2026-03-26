package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.BlockedCallDAO
import com.example.myapplication.data.dao.SearchHistoryDAO
import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDAO: SearchHistoryDAO,
    private val blockedCallDAO: BlockedCallDAO
) : HistoryRepository {

    override suspend fun insertSearch(searchHistory: SearchHistoryEntity) {
       searchHistoryDAO.insertSearch(searchHistory)
    }

    override suspend fun insertBlockedCall(blockedCall: BlockedCallEntity) {
        blockedCallDAO.insertBlockedCall(blockedCall)
    }

    override fun getAllHistorySearch(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDAO.getAllSearchHistory()
    }

    override fun getAllHistoryBlockedCalls(): Flow<List<BlockedCallEntity>> {
        return blockedCallDAO.getAllBlockedCalls()
    }


}