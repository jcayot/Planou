package com.cayot.flyingmore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flying-statistics")
data class FlyingStatisticEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val year: Int,
    val dataJson: String,
    val statisticTypeInt: Int
)
