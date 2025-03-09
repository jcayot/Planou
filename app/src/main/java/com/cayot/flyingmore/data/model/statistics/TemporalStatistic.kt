package com.cayot.flyingmore.data.model.statistics

import com.cayot.flyingmore.data.local.model.FlyingStatisticEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class TemporalStatistic(
    val name: String,
    val temporality: Temporality,
    val data: List<Int>,
    val suffix: String
)

//TODO Optimize serialization deserialization ???
object GsonProvider {
    val gson: Gson = Gson()
}

fun TemporalStatistic.toFlyingStatisticEntity() : FlyingStatisticEntity{
    return(FlyingStatisticEntity(
        name = name,
        temporalityInt = temporality.ordinal,
        dataJson = GsonProvider.gson.toJson(data),
        suffix = suffix
    ))
}

fun FlyingStatisticEntity.toTemporalStatistic() : TemporalStatistic{
    val jsonDataType = object : TypeToken<List<Int>>() {}.type
    return (TemporalStatistic(
        name = name,
        temporality = Temporality.entries[temporalityInt],
        data = GsonProvider.gson.fromJson(dataJson, jsonDataType),
        suffix = suffix
    ))
}