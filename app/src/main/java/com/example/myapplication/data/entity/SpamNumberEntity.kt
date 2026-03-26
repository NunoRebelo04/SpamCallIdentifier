package com.example.myapplication.data.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "spam_numbers",
    indices = [
        Index(value = ["phoneNumber"], unique = true)
    ]
)
data class SpamNumberEntity(
    @PrimaryKey
    val phoneNumber: String,
    val reportCount: Int = 0,
    val category: String? = null
)