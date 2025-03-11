package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Year
import java.util.HashMap
import kotlin.math.min

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
) {
    init {
        if (dataResolution == Resolution.DAILY) {
            if (data.size != (if (year.isLeap) 366 else 365))
                throw IllegalArgumentException("Invalid data")
        }
        throw IllegalArgumentException("Unimplemented initial resolution")
    }
}

fun <T> YearTemporalStatistic<T>.getResolution(resolution: Resolution) : List<T> {
    if (resolution < this.dataResolution)
        throw IllegalArgumentException("Cannot lower data resolution")

    if (this.dataResolution == Resolution.DAILY) {
        return (when (resolution) {
            Resolution.DAILY -> data
            Resolution.WEEKLY -> dailyToWeekly()
            Resolution.MONTHLY -> dailyToMonthly()
            Resolution.YEARLY -> dailyToYearly()
        })
    }
    throw IllegalStateException("Unimplemented data resolution. Shouldn't be constructable")
}

private fun <T> YearTemporalStatistic<T>.dailyToWeekly() : List<T> {
    val firstDayOfYear = this.year.atDay(1).dayOfWeek.ordinal
    val numberOfDays = this.data.size

    var weeklyData: MutableList<T> = mutableListOf()
    var startOfWeek = 0
    while (startOfWeek < numberOfDays) {
        var endOfWeek = if (startOfWeek == 0) 8 - firstDayOfYear else min(startOfWeek + 7, numberOfDays)
        weeklyData.add(this.sumData(this.data.subList(startOfWeek, endOfWeek)))
        startOfWeek = endOfWeek
    }
    return (weeklyData)
}

private fun <T> YearTemporalStatistic<T>.dailyToMonthly() : List<T> {
    val numberOfDays = this.data.size

    var monthlyData: MutableList<T> = mutableListOf()
    var startOfMonth = 0
    var currentMonth = 1
    while (startOfMonth < numberOfDays) {
        var endOfMonth = startOfMonth + this.year.atMonth(currentMonth).lengthOfMonth()
        monthlyData.add(this.sumData(this.data.subList(startOfMonth, endOfMonth)))
        startOfMonth = endOfMonth
    }
    return (monthlyData)
}

private fun <T> YearTemporalStatistic<T>.dailyToYearly() : List<T> {
    return (listOf(this.sumData(this.data)))
}

//Really weird :P Could probably be improved
@Suppress("UNCHECKED_CAST")
private fun <T> YearTemporalStatistic<T>.sumData(data: List<T>) : T {
    try {
        when (this.listDataType) {
            ListDataType.INT -> {
                val typedData: List<Int> = data as List<Int>
                return (typedData.sum() as T)
            }
            ListDataType.MAP_STRING_INT -> {
                val typedData: List<Map<String, Int>> = data as List<Map<String, Int>>
                var result: MutableMap<String, Int> = HashMap<String, Int>()
                for (items in typedData) {
                    for (item in items) {
                        result.merge(item.key, item.value) { previous, new -> previous + new }
                    }
                }
                return (result as T)
            }
        }
    } catch (e: ClassCastException) { //More robust handling ? Wrong data that should be removed but is it really needed ?
        throw (e)
    }
}

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