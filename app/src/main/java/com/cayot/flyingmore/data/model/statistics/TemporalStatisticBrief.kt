package com.cayot.flyingmore.data.model.statistics

import androidx.annotation.StringRes
import com.cayot.flyingmore.data.model.statistics.enums.ChartType

data class TemporalStatisticBrief(
    val id: Int = 0,
    val data: List<Int>,
    @StringRes val displayNameRes: Int,
    @StringRes val unitRes: Int? = null,
    val timeFrameString: String,
    val chartType: ChartType,
    val dataText: String,
    val trend: Trend? = null
)
