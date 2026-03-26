package com.example.myapplication.data.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "blocked_calls",
    indices=[
        Index(value = ["phoneNumber"])
    ]
)

data class BlockedCallEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phoneNumber: String,
    val category: String? = null
)