package com.cayot.planou.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
class SelectableDatesTo(private val time: Instant): SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return (utcTimeMillis <= time.toEpochMilli())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class SelectableDatesFrom(private val time: Instant): SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return (utcTimeMillis >= time.toEpochMilli())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class SelectableDateAll() : SelectableDates {

}