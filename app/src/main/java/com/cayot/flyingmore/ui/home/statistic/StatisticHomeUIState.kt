package com.cayot.flyingmore.ui.home.statistic

import com.cayot.flyingmore.data.model.statistics.TemporalStatisticBrief

data class StatisticHomeUIState(
    val statisticsList: List<TemporalStatisticBrief> = emptyList()
)