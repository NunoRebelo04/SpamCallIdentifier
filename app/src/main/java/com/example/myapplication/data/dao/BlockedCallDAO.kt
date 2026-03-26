package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.myapplication.data.entity.BlockedCallEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedCallDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertBlockedCall(blockedCall: BlockedCallEntity)

    @Query("SELECT * FROM blocked_calls ORDER BY id DESC")
    fun getAllBlockedCalls(): Flow<List<BlockedCallEntity>>

}