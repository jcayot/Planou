package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import java.time.Year

data class TemporalStatisticBrief(
    val id: Int = 0,
    val name: String,
    val year: Year,
    val unit: String? = null,
    val data: List<Int>,
    val chartType: ChartType,
    val dataText: String? = null
)
