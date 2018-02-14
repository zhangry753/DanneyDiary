package cn.zry.danneydiary.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zry on 2018/2/2.
 */
object DateUtils {
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日")
    private val monthFormat = SimpleDateFormat("yyyy年MM月")

    fun str2Month(dateStr: String) = monthFormat.parse(dateStr)
    fun str2Date(dateStr: String) = dateFormat.parse(dateStr)
    fun date2Str(date: Date) = dateFormat.format(date)
    fun date2MonthStr(date: Date) = monthFormat.format(date)
    fun date2Date(date: Date) = dateFormat.parse(dateFormat.format(date))
    fun date2Month(date: Date) = monthFormat.parse(monthFormat.format(date))
}