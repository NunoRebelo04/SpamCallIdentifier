package com.example.myapplication.di

import android.content.Context
import android.util.Log
import androidx.compose.ui.viewinterop.AndroidView
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.myapplication.data.dao.BlockedCallDAO
import com.example.myapplication.data.dao.SearchHistoryDAO
import com.example.myapplication.data.dao.SpamNumberDAO
import com.example.myapplication.data.entity.SpamNumberEntity
import com.example.myapplication.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        Log.d("DB_TEST", "Creating database instance")

        val dbFile = context.getDatabasePath("fraud_call_db.db")

        val database= Room.databaseBuilder<AppDatabase>(
            context=context,
            name=dbFile.absolutePath
        )
            .setDriver(AndroidSQLiteDriver())
            .build()
        prepolutateSpamNumbers(database)

        return database
    }

    private fun prepolutateSpamNumbers(database: AppDatabase){
        val scope= CoroutineScope(SupervisorJob()+ Dispatchers.IO)

        scope.launch {
            val spamDAO = database.spamNumberDao()

            val existingNumbers=spamDAO.getAllSpamNumbers()

            if(existingNumbers.isEmpty()){
                spamDAO.insertAll(
                    listOf(
                        SpamNumberEntity(
                            phoneNumber = "918876579",
                            reportCount = 10,
                            category = "Fraud"
                        ),
                        SpamNumberEntity(
                            phoneNumber = "960144155",
                            reportCount = 5,
                            category = "Spam"
                        )
                    )
                )
                Log.d("DB_TEST","INITIAL SPAM NUMBERS INSERTED")
            }
            else{
                Log.d("DB_TEST","DATABASE ALREADY HAS SPAM NUMBERS, SKIPPING SEED")

            }
        }
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