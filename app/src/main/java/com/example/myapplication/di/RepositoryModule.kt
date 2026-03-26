package com.example.myapplication.di

import com.example.myapplication.data.repository.HistoryRepository
import com.example.myapplication.data.repository.HistoryRepositoryImpl
import com.example.myapplication.data.repository.SpamRepository
import com.example.myapplication.data.repository.SpamRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSpamRepository(
        impl: SpamRepositoryImpl
    ): SpamRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        impl: HistoryRepositoryImpl
    ): HistoryRepository
}
