package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import java.time.Year
import kotlin.collections.iterator

class MapStringNumberTemporalStatistic(
    id: Int = 0,
    name: String,
    year: Year,
    dataResolution: Resolution,
    defaultDisplayResolution: Resolution,
    allowedDisplayResolutions: List<Resolution>,
    data: List<Map<String, Int>>,
    chartType: ChartType,
    unit: String
) : YearTemporalStatistic<Map<String, Int>>(
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
    override fun sumData(data : List<Map<String, Int>>): Map<String, Int> {
        var result: MutableMap<String, Int> = HashMap<String, Int>()
        for (items in data) {
            for (item in items) {
                result.merge(item.key, item.value) { previous, new -> previous + new }
            }
        }
        return (result)
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
            dataTypeInt = ListDataType.MAP_STRING_INT.ordinal,
            chartTypeInt = chartType.ordinal,
            unit = unit
        ))
    }

    override fun toTemporalStatisticBrief(resolution: Resolution): TemporalStatisticBrief {
        val rawData = getResolution(resolution)[0]
        val sortedValues = rawData.values.sortedDescending()
        val listOfBiggest = sortedValues.take(10)
        val keyWithMaxValue = rawData.maxByOrNull { it.value }?.key
        return TemporalStatisticBrief(
            id = id,
            name = name,
            year = year,
            unit = unit,
            data = listOfBiggest,
            chartType = chartType,
            dataText = keyWithMaxValue
        )
    }
}