package com.cayot.flyingmore.data.model.statistics

import androidx.annotation.StringRes
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import java.time.Year

data class TemporalStatisticBrief(
    val id: Int = 0,
    val year: Year,
    val data: List<Int>,
    @StringRes val displayNameRes: Int,
    @StringRes val unitRes: Int? = null,
    val chartType: ChartType,
    val dataText: String? = null
)
