package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import java.time.Year

class NumberYearTemporalStatistic(
    id: Int = 0,
    year: Year,
    data: List<Int>,
    statisticType: FlyingStatistic,
) : YearTemporalStatistic<Int>(
    id = id,
    year = year,
    data = data,
    statisticType = statisticType
) {
    override fun sumData(data : List<Int>): Int {
        return (data.sum())
    }

    override fun toFlyingStatisticEntity(): FlyingStatisticEntity {
        return(FlyingStatisticEntity(
            id = id,
            year = year.value,
            dataJson = GsonProvider.gson.toJson(data),
            statisticTypeInt = statisticType.ordinal
        ))
    }

    override fun toTemporalStatisticBrief(resolution: Resolution): TemporalStatisticBrief {
        return (TemporalStatisticBrief(
            id = id,
            year = year,
            data = this.getResolution(resolution),
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            chartType = statisticType.chartType
        ))
    }

    fun copy(data: List<Int> = this.data): NumberYearTemporalStatistic {
        return (NumberYearTemporalStatistic(
            id = id,
            year = year,
            data = data,
            statisticType = statisticType
        ))
    }
}
