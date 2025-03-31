package com.cayot.flyingmore.data.model.statistics

import androidx.annotation.ColorRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.cayot.flyingmore.R

enum class Trend(@ColorRes val colorRes: Int, val icon: ImageVector) {
    INCREASING(R.color.good_green, Icons.AutoMirrored.Filled.TrendingUp),
    DECREASING(R.color.bad_red, Icons.AutoMirrored.Filled.TrendingDown),
    STABLE(R.color.stable_grey, Icons.AutoMirrored.Filled.TrendingFlat)
}