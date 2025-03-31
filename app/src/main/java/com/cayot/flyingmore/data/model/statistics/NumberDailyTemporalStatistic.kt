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
        return (TemporalStatisticBrief(
            id = id,
            data = this.getData(statisticType.briefDisplayResolution),
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            timeFrameName = this.getTimeFrameString(statisticType.briefDisplayResolution),
            chartType = statisticType.chartType
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
