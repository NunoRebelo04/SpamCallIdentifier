package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.SpamNumberEntity

interface SpamRepository {

    suspend fun getSpamNumberByPhoneNumber(phoneNumber : String) : SpamNumberEntity?

    suspend fun isSpamNumber(phoneNumber: String) : Boolean

    suspend fun insertSpamNumbers(spamNumbers : List<SpamNumberEntity>)

    suspend fun getAllSpamNumbers(): List<SpamNumberEntity>
}