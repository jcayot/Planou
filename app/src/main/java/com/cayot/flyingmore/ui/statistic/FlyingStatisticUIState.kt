package com.cayot.flyingmore.ui.statistic

import com.cayot.flyingmore.data.model.statistics.DailyTemporalStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame

data class FlyingStatisticUIState(
    val statisticData: DailyTemporalStatistic<Any>? = null,
    val displayResolution: TimeFrame = TimeFrame.MONTH
)
