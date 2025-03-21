package com.cayot.flyingmore.workers

import androidx.work.Data

fun Data.getIntOrThrow(key: String, errorValue: Int = -1) : Int {
    val value = this.getInt(key, errorValue)
    if (value == errorValue)
        throw IllegalArgumentException("Key $key is mandatory")
    return (value)
}
