package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.myapplication.data.entity.SpamNumberEntity

@Dao
interface SpamNumberDAO {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSpamNumber(spamNumber: SpamNumberEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(spamNumbers: List<SpamNumberEntity>)

    @Query("SELECT * FROM spam_numbers WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getSpamNumberByPhoneNumber(phoneNumber: String): SpamNumberEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM spam_numbers WHERE phoneNumber = :phoneNumber)")
    suspend fun isSpamNumber(phoneNumber: String): Boolean

    @Query("SELECT * FROM spam_numbers ORDER BY category ASC, phoneNumber ASC")
    suspend fun getAllSpamNumbers(): List<SpamNumberEntity>


}