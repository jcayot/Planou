package com.cayot.flyingmore.data.model.statistics.enums

import androidx.annotation.StringRes
import com.cayot.flyingmore.R

enum class TimeFrame(
    @StringRes val displayNameResource: Int,
) {
    DAY(R.string.time_frame_day),
    WEEK(R.string.time_frame_week),
    MONTH(R.string.time_frame_month),
    YEAR(R.string.time_frame_year)
}