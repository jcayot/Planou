package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.cayot.flyingmore.data.model.statistics.enums.FlyingStatistic
import com.cayot.flyingmore.data.model.statistics.enums.ListDataType
import com.cayot.flyingmore.data.model.statistics.enums.TimeFrame
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.min

//TODO Better than cast ?
abstract class DailyTemporalStatistic<T>(
    val id: Int = 0,
    val timeFrameStart: LocalDate,
    val timeFrameEnd: LocalDate,
    val data: List<T>,
    val statisticType: FlyingStatistic,
    ) {
    init {
        if (statisticType.dataResolution != TimeFrame.DAY)
            throw IllegalArgumentException("Unimplemented initial resolution")
        if (statisticType.dataTimeFrame != TimeFrame.YEAR)
            throw IllegalArgumentException("Unimplemented time frame")
        if (data.size != ChronoUnit.DAYS.between(timeFrameStart, timeFrameEnd).toInt())
            throw IllegalArgumentException("Invalid data length")
    }

    companion object {
        fun <T> makeDailyTemporalStatistic(
            id: Int = 0,
            timeFrameStart: LocalDate,
            timeFrameEnd: LocalDate,
            data: List<T>,
            statisticType: FlyingStatistic
        ) : DailyTemporalStatistic<T> {
            @Suppress("UNCHECKED_CAST")
            return when (statisticType.dataType) {
                ListDataType.INT -> NumberDailyTemporalStatistic(
                    id = id,
                    timeFrameStart = timeFrameStart,
                    timeFrameEnd = timeFrameEnd,
                    data = data as List<Int>,
                    statisticType = statisticType
                )

                ListDataType.MAP_STRING_INT -> MapStringNumberDailyTemporalStatistic(
                    id = id,
                    timeFrameStart = timeFrameStart,
                    timeFrameEnd = timeFrameEnd,
                    data = data as List<Map<String, Int>>,
                    statisticType = statisticType
                )
            } as DailyTemporalStatistic<T>
        }
    }

    abstract fun sumData(data : List<T>) : T

    abstract fun toFlyingStatisticEntity() : FlyingStatisticEntity

    abstract fun toTemporalStatisticBrief(resolution: TimeFrame = TimeFrame.MONTH) : TemporalStatisticBrief

    abstract fun copy(data: List<T> = this.data) : DailyTemporalStatistic<T>
}

fun <T> DailyTemporalStatistic<T>.getResolution(resolution: TimeFrame = statisticType.dataResolution) : List<T> {
    if (resolution < statisticType.dataResolution)
        throw IllegalArgumentException("Cannot lower data resolution")

    if (statisticType.dataResolution == TimeFrame.DAY) {
        return (when (resolution) {
            TimeFrame.DAY -> data
            TimeFrame.WEEK -> dailyToWeekly()
            TimeFrame.MONTH -> dailyToMonthly()
            TimeFrame.YEAR -> dailyToYearly()
        })
    }
    throw IllegalStateException("Unimplemented data resolution. Shouldn't be constructable")
}

fun <T> DailyTemporalStatistic<T>.getTimeFrameName() : String {
    return when (statisticType.dataTimeFrame) {
        TimeFrame.DAY -> timeFrameStart.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        TimeFrame.WEEK -> "${formatWeekDate(timeFrameStart)} - ${formatWeekDate(timeFrameEnd)}"
        TimeFrame.MONTH -> timeFrameStart.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        TimeFrame.YEAR -> timeFrameStart.year.toString()
    }
}

private fun <T> DailyTemporalStatistic<T>.formatWeekDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())
    return date.format(formatter)
}

private fun <T> DailyTemporalStatistic<T>.dailyToWeekly() : List<T> {
    val firstDay = this.timeFrameStart.dayOfWeek.ordinal
    val numberOfDays = this.data.size

    var weeklyData: MutableList<T> = mutableListOf()
    var startOfWeek = 0
    while (startOfWeek < numberOfDays) {
        var endOfWeek = if (startOfWeek == 0) 7 - firstDay else min(startOfWeek + 7, numberOfDays)
        weeklyData.add(this.sumData(this.data.subList(startOfWeek, endOfWeek)))
        startOfWeek = endOfWeek
    }
    return (weeklyData)
}

private fun <T> DailyTemporalStatistic<T>.dailyToMonthly() : List<T> {
    val year: Year = Year.of(timeFrameStart.year)
    val numberOfDays = this.data.size

    var monthlyData: MutableList<T> = mutableListOf()
    var startOfMonth = 0
    var currentMonth = timeFrameStart.monthValue
    while (startOfMonth < numberOfDays) {
        var endOfMonth = min(startOfMonth + year.atMonth(currentMonth).lengthOfMonth(), numberOfDays)
        monthlyData.add(this.sumData(this.data.subList(startOfMonth, endOfMonth)))
        startOfMonth = endOfMonth
        currentMonth++
    }
    return (monthlyData)
}

private fun <T> DailyTemporalStatistic<T>.dailyToYearly() : List<T> {
    return (listOf(this.sumData(this.data)))
}

object GsonProvider {
    val gson: Gson = Gson()
}

fun <T> FlyingStatisticEntity.toTemporalStatistic() : DailyTemporalStatistic<T>{
    val statisticType = FlyingStatistic.entries[statisticTypeInt]
    val dataJsonType = ListDataType.getType(statisticType.dataType)
    @Suppress("UNCHECKED_CAST")
    return (when (statisticType.dataType) {
        ListDataType.INT -> NumberDailyTemporalStatistic(
            id = id,
            timeFrameStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeFrameStartLong),
                ZoneOffset.UTC).toLocalDate(),
            timeFrameEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeFrameEndLong),
                ZoneOffset.UTC).toLocalDate(),
            data = GsonProvider.gson.fromJson(dataJson, dataJsonType),
            statisticType = statisticType
        )
        ListDataType.MAP_STRING_INT -> MapStringNumberDailyTemporalStatistic(
            id = id,
            timeFrameStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeFrameStartLong),
                ZoneOffset.UTC).toLocalDate(),
            timeFrameEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeFrameEndLong),
                ZoneOffset.UTC).toLocalDate(),
            data = GsonProvider.gson.fromJson(dataJson, dataJsonType),
            statisticType = statisticType
        )
    } as DailyTemporalStatistic<T>)
}