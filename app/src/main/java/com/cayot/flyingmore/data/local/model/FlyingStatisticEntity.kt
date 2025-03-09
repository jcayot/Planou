package com.cayot.flyingmore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//Replace storing in database by known and added to the object for some field ?
@Entity(tableName = "flying-statistics")
data class FlyingStatisticEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val year: Int,
    val dataResolutionInt: Int,
    val defaultDisplayResolutionInt: Int,
    val allowedDisplayResolutionsJson: String,
    val dataJson: String,
    val dataTypeInt: Int,
    val chartTypeInt: Int,
    val unit: String
)
