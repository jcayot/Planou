package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.ChartType
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import com.cayot.flyingmore.data.model.statistics.enums.Resolution
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Year
import kotlin.math.min

abstract class YearTemporalStatistic<T>(
    val id: Int = 0,
    val name: String,
    val year: Year,
    val dataResolution: Resolution,
    val defaultDisplayResolution: Resolution,
    val allowedDisplayResolutions: List<Resolution>,
    val data: List<T>,
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

    abstract fun sumData(data : List<T>) : T

    abstract fun toFlyingStatisticEntity() : FlyingStatisticEntity

    abstract fun toTemporalStatisticBrief(resolution: Resolution = Resolution.MONTHLY) : TemporalStatisticBrief
}

fun <T> YearTemporalStatistic<T>.getResolution(resolution: Resolution = this.dataResolution) : List<T> {
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

object GsonProvider {
    val gson: Gson = Gson()
}

fun <T> FlyingStatisticEntity.toTemporalStatistic() : YearTemporalStatistic<T>{
    val allowedDisplayTemporalitiesJsonType = object : TypeToken<List<Resolution>>() {}.type
    val listDataType = ListDataType.entries[dataTypeInt]
    val dataJsonType = ListDataType.getType(listDataType)
    return (when (listDataType) {
        ListDataType.INT -> NumberYearTemporalStatistic(
            id = id,
            name = name,
            year = Year.of(year),
            dataResolution = Resolution.entries[dataResolutionInt],
            defaultDisplayResolution = Resolution.entries[defaultDisplayResolutionInt],
            allowedDisplayResolutions = GsonProvider.gson.fromJson(allowedDisplayResolutionsJson, allowedDisplayTemporalitiesJsonType),
            data = GsonProvider.gson.fromJson(dataJson, dataJsonType),
            chartType = ChartType.entries[chartTypeInt],
            unit = unit
        )
        ListDataType.MAP_STRING_INT -> MapStringNumberTemporalStatistic(
            id = id,
            name = name,
            year = Year.of(year),
            dataResolution = Resolution.entries[dataResolutionInt],
            defaultDisplayResolution = Resolution.entries[defaultDisplayResolutionInt],
            allowedDisplayResolutions = GsonProvider.gson.fromJson(allowedDisplayResolutionsJson, allowedDisplayTemporalitiesJsonType),
            data = GsonProvider.gson.fromJson(dataJson, dataJsonType),
            chartType = ChartType.entries[chartTypeInt],
            unit = unit
        )
    } as YearTemporalStatistic<T>)
}