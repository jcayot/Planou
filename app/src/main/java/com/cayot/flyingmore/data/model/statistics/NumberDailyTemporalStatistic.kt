package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
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

    override fun toTemporalStatisticBrief(): TemporalStatisticBrief {
        val fullData = this.getData(statisticType.briefDisplayResolution)
        val data = fullData.dropLastWhile { it == 0 }
        val dataToDisplay = data.takeLast(5)

        return (TemporalStatisticBrief(
            id = id,
            data = data,
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            timeFrameString = this.getTimeFrameString(statisticType.briefDisplayResolution, data.size),
            chartType = statisticType.chartType,
            dataText = data.last().toString(),
            trend = getTrend(dataToDisplay)
        ))
    }

    override fun copy(data: List<Int>): NumberDailyTemporalStatistic {
        return (NumberDailyTemporalStatistic(
            id = id,
            timeFrameStart = timeFrameStart,
            timeFrameEnd = timeFrameEnd,
            data = data,
            statisticType = statisticType
        ))
    }
}

private fun NumberDailyTemporalStatistic.getTrend(data : List<Int>) : Trend? {
    if (data.size < 2)
        return null
    if (data.last() > data[data.size - 2])
        return Trend.INCREASING
    if (data.last() < data[data.size - 2])
        return Trend.DECREASING
    return Trend.STABLE
}
