package com.example.myapplication.service


import android.telecom.Call
import android.telecom.CallScreeningService
import com.example.myapplication.data.entity.BlockedCallEntity
import com.example.myapplication.data.repository.HistoryRepository
import com.example.myapplication.data.repository.SpamRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SpamCallScreeningService : CallScreeningService(){

    @Inject
    lateinit var spamRepository: SpamRepository

    @Inject
    lateinit var historyRepository: HistoryRepository

    private val serviceScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: run {
            allowCall(callDetails)
            return
        }

        serviceScope.launch {
            val spamInfo = spamRepository.getSpamNumberByPhoneNumber(phoneNumber)

            if (spamInfo != null) {
                blockCall(callDetails)

                historyRepository.insertBlockedCall(
                    BlockedCallEntity(
                        phoneNumber = phoneNumber,
                        category = spamInfo.category
                    )
                )
            } else {
                allowCall(callDetails)
            }
        }
    }

    private fun blockCall(callDetails: Call.Details) {
        val response = CallScreeningService.CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()

        respondToCall(callDetails, response)
    }

    private fun allowCall(callDetails: Call.Details) {
        val response = CallScreeningService.CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .build()

        respondToCall(callDetails, response)
    }

}