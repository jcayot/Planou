package com.cayot.flyingmore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flying-statistics")
data class FlyingStatisticEntity(
    @PrimaryKey
    val name: String,
    val temporalityInt: Int,
    val dataJson: String,
    val suffix: String
)
