package com.example.myapplication.di

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.myapplication.data.dao.BlockedCallDAO
import com.example.myapplication.data.dao.SearchHistoryDAO
import com.example.myapplication.data.dao.SpamNumberDAO
import com.example.myapplication.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        android.util.Log.d("DB_TEST", "Creating database instance")

        val dbFile = context.getDatabasePath("fraud_call_db.db")

        return Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(AndroidSQLiteDriver())
            .build()
    }

    @Provides
    fun provideSpamNumberDAO(database: AppDatabase): SpamNumberDAO{
        return database.spamNumberDao()
    }

    @Provides
    fun provideSearchHistoryDAO(database: AppDatabase) : SearchHistoryDAO{
        return database.searchHistoryDao()
    }

    @Provides
    fun provideBlockedCallsDAO(database: AppDatabase) : BlockedCallDAO{
        return database.blockedCallDao()
    }
}