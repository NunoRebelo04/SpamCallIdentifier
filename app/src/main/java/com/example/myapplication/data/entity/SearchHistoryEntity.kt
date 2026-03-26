package com.example.myapplication.data.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "search_history",
    indices=[
        Index(value = ["phoneNumber"])
    ]
)

data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val phoneNumber : String,
    val isSpam: Boolean
)