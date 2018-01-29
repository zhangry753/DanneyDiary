package cn.zry.danneydiary.room

import android.arch.persistence.room.TypeConverter
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zry on 2018/1/29.
 */
class RoomConverters {
    @TypeConverter
    open fun arrayToString(array: Array<String>): String {
        if (array == null || array.size === 0) {
            return ""
        }
        val builder = StringBuilder(array[0])
        for (i in 1..array.size - 1) {
            builder.append(",").append(array[i])
        }
        return builder.toString()
    }
    @TypeConverter
    open fun StringToArray(value: String): Array<String>? {
        return if (TextUtils.isEmpty(value)) null else value.split(",").toTypedArray()

    }

    @TypeConverter
    open fun StringToDate(value: String): Date? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value)

    }
    @TypeConverter
    open fun DateToString(value: Date): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value)
    }
}