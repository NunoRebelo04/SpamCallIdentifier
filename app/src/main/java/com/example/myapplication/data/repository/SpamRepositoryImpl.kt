package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.SpamNumberDAO
import com.example.myapplication.data.entity.SpamNumberEntity
import javax.inject.Inject

class SpamRepositoryImpl @Inject constructor(
    private val spamNumberDAO: SpamNumberDAO
): SpamRepository{
    override suspend fun getSpamNumberByPhoneNumber(phoneNumber: String): SpamNumberEntity? {
        return spamNumberDAO.getSpamNumberByPhoneNumber(phoneNumber)
    }

    override suspend fun isSpamNumber(phoneNumber: String): Boolean {
        return spamNumberDAO.isSpamNumber(phoneNumber)
    }

    override suspend fun insertSpamNumbers(spamNumbers: List<SpamNumberEntity>) {
        return spamNumberDAO.insertAll(spamNumbers)
    }

    override suspend fun getAllSpamNumbers(): List<SpamNumberEntity> {
        return spamNumberDAO.getAllSpamNumbers()
    }

}