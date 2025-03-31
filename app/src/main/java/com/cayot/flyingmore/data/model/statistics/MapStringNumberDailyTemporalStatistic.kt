package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import java.time.LocalDate
import java.time.ZoneOffset

class MapStringNumberDailyTemporalStatistic(
    id: Int = 0,
    timeFrameStart: LocalDate,
    timeFrameEnd: LocalDate,
    data: List<Map<String, Int>>,
    statisticType: FlyingStatistic
) : DailyTemporalStatistic<Map<String, Int>>(
    id = id,
    timeFrameStart = timeFrameStart,
    timeFrameEnd = timeFrameEnd,
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
            timeFrameStartLong = timeFrameStart.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            timeFrameEndLong = timeFrameEnd.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            dataJson = GsonProvider.gson.toJson(data),
            statisticTypeInt = statisticType.ordinal
        ))
    }

    override fun toTemporalStatisticBrief(): TemporalStatisticBrief {
        val rawData = getData(statisticType.briefDisplayResolution)[0]
        val sortedValues = rawData.values.sortedDescending()
        val listOfBiggest = sortedValues.take(10)
        val keyWithMaxValue = rawData.maxByOrNull { it.value }?.key
        return TemporalStatisticBrief(
            id = id,
            data = listOfBiggest,
            displayNameRes = statisticType.displayNameResource,
            unitRes = statisticType.unitResource,
            timeFrameString = this.getTimeFrameString(statisticType.briefDisplayResolution),
            chartType = statisticType.chartType,
            dataText = keyWithMaxValue + " " + listOfBiggest[0],
        )
    }

    override fun copy(data: List<Map<String, Int>>): MapStringNumberDailyTemporalStatistic {
        return (MapStringNumberDailyTemporalStatistic(
            id = id,
            timeFrameStart = timeFrameStart,
            timeFrameEnd = timeFrameEnd,
            data = data,
            statisticType = statisticType
        ))
    }
}