package com.cayot.flyingmore.ui.home.statistics

import com.cayot.flyingmore.data.model.statistics.TemporalStatisticBrief

data class StatisticsHomeUIState(
    val statisticsList: List<TemporalStatisticBrief> = emptyList()
)