package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.myapplication.data.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSearch(searchHistory: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

}