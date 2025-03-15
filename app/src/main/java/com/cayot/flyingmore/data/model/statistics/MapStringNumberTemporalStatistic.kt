package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import java.time.Year

class MapStringNumberTemporalStatistic(
    id: Int = 0,
    year: Year,
    data: List<Map<String, Int>>,
    statisticType: FlyingStatistic
) : YearTemporalStatistic<Map<String, Int>>(
    id = id,
    year = year,
    data = data,
    statisticType = statisticType
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
            year = year.value,
            dataJson = GsonProvider.gson.toJson(data),
            statisticTypeInt = statisticType.ordinal
        ))
    }

    override fun toTemporalStatisticBrief(resolution: Resolution): TemporalStatisticBrief {
        val rawData = getResolution(resolution)[0]
        val sortedValues = rawData.values.sortedDescending()
        val listOfBiggest = sortedValues.take(10)
        val keyWithMaxValue = rawData.maxByOrNull { it.value }?.key
        return TemporalStatisticBrief(
            id = id,
            year = year,
            data = listOfBiggest,
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            chartType = statisticType.chartType,
            dataText = keyWithMaxValue
        )
    }

    fun copy(data: List<Map<String, Int>> = this.data): MapStringNumberTemporalStatistic {
        return (MapStringNumberTemporalStatistic(
            id = id,
            year = year,
            data = data,
            statisticType = statisticType
        ))
    }
}