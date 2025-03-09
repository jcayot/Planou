package com.cayot.flyingmore.data.model.statistics

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

enum class ListDataType(val type: Type) {
    INT(object : TypeToken<List<Int>>() {}.type),
    MAP_STRING_INT(object : TypeToken<List<Map<String, Int>>>() {}.type);

    companion object {
        fun getType(listDataType: ListDataType): Type {
            return listDataType.type
        }
    }
}
