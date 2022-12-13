package com.example.photoediting.data.offline.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity(tableName = "transactionTb")
class TransactionEntity (

    @field:ColumnInfo("photoUrl")
    val photoUrl: String,

    @field:ColumnInfo("createdAt")
    val createdAt: String,

    @field:ColumnInfo("name")
    val name: String,

    @field:ColumnInfo("description")
    val description: String,

    @field:ColumnInfo("lon")
    val lon: Double,

    @field:PrimaryKey
    @field:ColumnInfo("id")
    val id: String,

    @field:ColumnInfo("lat")
    var lat: Double
)