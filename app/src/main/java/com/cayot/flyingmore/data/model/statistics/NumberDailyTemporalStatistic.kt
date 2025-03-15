package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import java.time.LocalDate
import java.time.ZoneOffset

class NumberDailyTemporalStatistic(
    id: Int = 0,
    timeFrameStart: LocalDate,
    timeFrameEnd: LocalDate,
    data: List<Int>,
    statisticType: FlyingStatistic,
) : DailyTemporalStatistic<Int>(
    id = id,
    timeFrameStart = timeFrameStart,
    timeFrameEnd = timeFrameEnd,
    data = data,
    statisticType = statisticType
) {
    override fun sumData(data : List<Int>): Int {
        return (data.sum())
    }

    override fun toFlyingStatisticEntity(): FlyingStatisticEntity {
        return(FlyingStatisticEntity(
            id = id,
            timeFrameStartLong = timeFrameStart.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            timeFrameEndLong = timeFrameEnd.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            dataJson = GsonProvider.gson.toJson(data),
            statisticTypeInt = statisticType.ordinal
        ))
    }

    override fun toTemporalStatisticBrief(resolution: TimeFrame): TemporalStatisticBrief {
        return (TemporalStatisticBrief(
            id = id,
            data = this.getResolution(resolution),
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            timeFrameName = this.getTimeFrameName(),
            chartType = statisticType.chartType
        ))
    }

    fun copy(data: List<Int> = this.data): NumberDailyTemporalStatistic {
        return (NumberDailyTemporalStatistic(
            id = id,
            timeFrameStart = timeFrameStart,
            timeFrameEnd = timeFrameEnd,
            data = data,
            statisticType = statisticType
        ))
    }
}
