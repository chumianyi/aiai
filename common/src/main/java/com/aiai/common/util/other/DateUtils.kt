/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 日期时间工具类。
 *
 * 提供完整的日期计算、格式化、比较能力。
 */
object DateUtils {

    private val defaultFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    // region 格式化

    /** 当前时间格式化字符串。 */
    fun now(): String = defaultFormat.format(Date())

    /** 时间戳格式化。 */
    fun formatTimestamp(timestamp: Long): String = defaultFormat.format(Date(timestamp))

    /** 日期格式化。 */
    fun formatDate(date: Date): String = dateFormat.format(date)

    /** 时间格式化。 */
    fun formatTime(date: Date): String = timeFormat.format(date)

    /** 自定义格式。 */
    fun format(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    // endregion

    // region 日期计算

    /** 今天 0 点的时间戳。 */
    fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /** 明天 0 点的时间戳。 */
    fun tomorrowStart(): Long = todayStart() + 24 * 60 * 60 * 1000L

    /** 昨天 0 点的时间戳。 */
    fun yesterdayStart(): Long = todayStart() - 24 * 60 * 60 * 1000L

    /** 一周前的时间戳。 */
    fun weekAgo(): Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L

    /** 一个月前的时间戳。 */
    fun monthAgo(): Long = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L

    // endregion

    // region 比较

    /** 是否为今天。 */
    fun isToday(timestamp: Long): Boolean {
        return formatTimestamp(timestamp) == formatTimestamp(System.currentTimeMillis())
    }

    /** 是否为昨天。 */
    fun isYesterday(timestamp: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance().apply { timeInMillis = yesterdayStart() }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /** 是否为今年。 */
    fun isThisYear(timestamp: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance()
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }

    // endregion

    // region 星期

    /** 获取星期几（中文）。 */
    fun getWeekdayChinese(date: Date): String {
        val cal = Calendar.getInstance().apply { time = date }
        return when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "周日"
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            Calendar.SATURDAY -> "周六"
            else -> ""
        }
    }

    // endregion
}
