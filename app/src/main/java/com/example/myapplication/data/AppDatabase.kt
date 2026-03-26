package com.example.myapplication.data.local

import android.annotation.SuppressLint
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.myapplication.data.dao.BlockedCallDAO
import com.example.myapplication.data.dao.SearchHistoryDAO
import com.example.myapplication.data.dao.SpamNumberDAO
import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.entity.SearchHistoryEntity
import com.example.myapplication.data.entity.SpamNumberEntity

@SuppressLint("RestrictedApi")
@Database(
    entities = [
        SpamNumberEntity::class,
        SearchHistoryEntity::class,
        BlockedCallEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun spamNumberDao(): SpamNumberDAO
    abstract fun searchHistoryDao(): SearchHistoryDAO
    abstract fun blockedCallDao(): BlockedCallDAO
}