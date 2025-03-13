package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import java.time.Year

class NumberYearTemporalStatistic(
    id: Int = 0,
    name: String,
    year: Year,
    dataResolution: Resolution,
    defaultDisplayResolution: Resolution,
    allowedDisplayResolutions: List<Resolution>,
    data: List<Int>,
    chartType: ChartType,
    unit: String
) : YearTemporalStatistic<Int>(
    id = id,
    name = name,
    year = year,
    dataResolution = dataResolution,
    defaultDisplayResolution = defaultDisplayResolution,
    allowedDisplayResolutions = allowedDisplayResolutions,
    data = data,
    chartType = chartType,
    unit = unit
) {
    override fun sumData(data : List<Int>): Int {
        return (data.sum())
    }

    override fun toFlyingStatisticEntity(): FlyingStatisticEntity {
        return(FlyingStatisticEntity(
            id = id,
            name = name,
            year = year.value,
            dataResolutionInt = dataResolution.ordinal,
            defaultDisplayResolutionInt = defaultDisplayResolution.ordinal,
            allowedDisplayResolutionsJson = GsonProvider.gson.toJson(allowedDisplayResolutions),
            dataJson = GsonProvider.gson.toJson(data),
            dataTypeInt = ListDataType.INT.ordinal,
            chartTypeInt = chartType.ordinal,
            unit = unit
        ))
    }

    override fun toTemporalStatisticBrief(resolution: Resolution): TemporalStatisticBrief {
        return (TemporalStatisticBrief(
            id = id,
            name = name,
            year = year,
            unit = unit,
            data = this.getResolution(resolution),
            chartType = chartType
        ))
    }
}
