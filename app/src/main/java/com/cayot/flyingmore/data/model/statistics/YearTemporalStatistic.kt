package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Year

data class YearTemporalStatistic<T>(
    val id: Int = 0,
    val name: String,
    val year: Year,
    val dataResolution: Resolution,
    val defaultDisplayResolution: Resolution,
    val allowedDisplayResolutions: List<Resolution>,
    val data: List<T>,
    val listDataType: ListDataType,
    val chartType: ChartType,
    val unit: String
)

fun <T> YearTemporalStatistic<T>.getResolution(resolution: Resolution) : List<T> {
    if (resolution < this.dataResolution)
        throw IllegalArgumentException("Cannot lower data resolution")

    var modifiedData = this.data
    when {
        this.dataResolution <= Resolution.DAILY && resolution > Resolution.DAILY -> {

        }
        this.dataResolution <= Resolution.WEEKLY && resolution > Resolution.WEEKLY -> {

        }
        this.dataResolution <= Resolution.MONTHLY && resolution > Resolution.MONTHLY -> {

        }
        this.dataResolution <= Resolution.YEARLY && resolution > Resolution.YEARLY -> {

        }
    }
    return(modifiedData)
}

//TODO Optimize serialization deserialization ???
object GsonProvider {
    val gson: Gson = Gson()
}

fun <T> YearTemporalStatistic<T>.toFlyingStatisticEntity() : FlyingStatisticEntity{
    return(FlyingStatisticEntity(
        id = id,
        name = name,
        year = year.value,
        dataResolutionInt = dataResolution.ordinal,
        defaultDisplayResolutionInt = defaultDisplayResolution.ordinal,
        allowedDisplayResolutionsJson = GsonProvider.gson.toJson(allowedDisplayResolutions),
        dataJson = GsonProvider.gson.toJson(data),
        dataTypeInt = listDataType.ordinal,
        chartTypeInt = chartType.ordinal,
        unit = unit
    ))
}

fun <T> FlyingStatisticEntity.toTemporalStatistic() : YearTemporalStatistic<T>{
    val allowedDisplayTemporalitiesJsonType = object : TypeToken<List<Resolution>>() {}.type
    val listDataType = ListDataType.entries[dataTypeInt]
    val dataJsonType = ListDataType.getType(listDataType)
    return (YearTemporalStatistic(
        id = id,
        name = name,
        year = Year.of(year),
        dataResolution = Resolution.entries[dataResolutionInt],
        defaultDisplayResolution = Resolution.entries[defaultDisplayResolutionInt],
        allowedDisplayResolutions = GsonProvider.gson.fromJson(allowedDisplayResolutionsJson, allowedDisplayTemporalitiesJsonType),
        data = GsonProvider.gson.fromJson(dataJson, dataJsonType),
        listDataType = listDataType,
        chartType = ChartType.entries[chartTypeInt],
        unit = unit
    ))
}