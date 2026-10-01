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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 日期时间工具类。
 *
 * 提供日期格式化、解析、比较、计算等功能。
 */
object DateUtil {

    const val PATTERN_YMD = "yyyy-MM-dd"
    const val PATTERN_YMD_HM = "yyyy-MM-dd HH:mm"
    const val PATTERN_YMD_HMS = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_HM = "HH:mm"
    const val PATTERN_HMS = "HH:mm:ss"
    const val PATTERN_MD = "MM-dd"
    const val PATTERN_YMD_CN = "yyyy年MM月dd日"
    const val PATTERN_YMD_HM_CN = "yyyy年MM月dd日 HH:mm"

    private val locale = Locale.getDefault()

    /**
     * 格式化日期。
     *
     * @param timeMillis 时间戳
     * @param pattern 格式
     * @return 格式化后的日期字符串
     */
    fun format(timeMillis: Long, pattern: String = PATTERN_YMD_HMS): String {
        return try {
            SimpleDateFormat(pattern, locale).format(Date(timeMillis))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 解析日期字符串。
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 时间戳，解析失败返回 null
     */
    fun parse(dateStr: String?, pattern: String = PATTERN_YMD_HMS): Long? {
        if (dateStr.isNullOrBlank()) return null
        return try {
            SimpleDateFormat(pattern, locale).parse(dateStr)?.time
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取当前时间戳。
     *
     * @return 当前时间戳
     */
    fun now(): Long = System.currentTimeMillis()

    /**
     * 获取今天开始的时间戳。
     *
     * @return 今天开始的时间戳
     */
    fun todayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * 获取今天结束的时间戳。
     *
     * @return 今天结束的时间戳
     */
    fun todayEnd(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    /**
     * 是否是今天。
     *
     * @param timeMillis 时间戳
     * @return 是否是今天
     */
    fun isToday(timeMillis: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timeMillis }
        val calendar2 = Calendar.getInstance()
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 是否是昨天。
     *
     * @param timeMillis 时间戳
     * @return 是否是昨天
     */
    fun isYesterday(timeMillis: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timeMillis }
        val calendar2 = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 是否是今年。
     *
     * @param timeMillis 时间戳
     * @return 是否是今年
     */
    fun isThisYear(timeMillis: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timeMillis }
        val calendar2 = Calendar.getInstance()
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
    }

    /**
     * 友好时间显示。
     *
     * @param timeMillis 时间戳
     * @return 友好时间字符串
     */
    fun friendlyTime(timeMillis: Long): String {
        val now = now()
        val diff = now - timeMillis

        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
            isYesterday(timeMillis) -> "昨天 ${format(timeMillis, PATTERN_HM)}"
            isThisYear(timeMillis) -> format(timeMillis, PATTERN_MD_HM)
            else -> format(timeMillis, PATTERN_YMD)
        }
    }

    private const val PATTERN_MD_HM = "MM-dd HH:mm"

    /**
     * 计算两个日期之间的天数差。
     *
     * @param start 开始时间戳
     * @param end 结束时间戳
     * @return 天数差
     */
    fun daysBetween(start: Long, end: Long): Long {
        val diff = Math.abs(end - start)
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    /**
     * 计算两个日期之间的小时差。
     *
     * @param start 开始时间戳
     * @param end 结束时间戳
     * @return 小时差
     */
    fun hoursBetween(start: Long, end: Long): Long {
        val diff = Math.abs(end - start)
        return TimeUnit.MILLISECONDS.toHours(diff)
    }

    /**
     * 计算两个日期之间的分钟差。
     *
     * @param start 开始时间戳
     * @param end 结束时间戳
     * @return 分钟差
     */
    fun minutesBetween(start: Long, end: Long): Long {
        val diff = Math.abs(end - start)
        return TimeUnit.MILLISECONDS.toMinutes(diff)
    }

    /**
     * 添加天数。
     *
     * @param timeMillis 原始时间戳
     * @param days 要添加的天数
     * @return 新的时间戳
     */
    fun addDays(timeMillis: Long, days: Int): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.timeInMillis
    }

    /**
     * 添加月份。
     *
     * @param timeMillis 原始时间戳
     * @param months 要添加的月份
     * @return 新的时间戳
     */
    fun addMonths(timeMillis: Long, months: Int): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        calendar.add(Calendar.MONTH, months)
        return calendar.timeInMillis
    }

    /**
     * 添加年份。
     *
     * @param timeMillis 原始时间戳
     * @param years 要添加的年份
     * @return 新的时间戳
     */
    fun addYears(timeMillis: Long, years: Int): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        calendar.add(Calendar.YEAR, years)
        return calendar.timeInMillis
    }

    /**
     * 获取星期几。
     *
     * @param timeMillis 时间戳
     * @return 星期几（1-7，1为周日）
     */
    fun dayOfWeek(timeMillis: Long): Int {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    /**
     * 获取星期几的名称。
     *
     * @param timeMillis 时间戳
     * @return 星期名称
     */
    fun dayOfWeekName(timeMillis: Long): String {
        return when (dayOfWeek(timeMillis)) {
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

    /**
     * 获取月份名称。
     *
     * @param timeMillis 时间戳
     * @return 月份名称
     */
    fun monthName(timeMillis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }
        return "${calendar.get(Calendar.MONTH) + 1}月"
    }

    /**
     * 是否是闰年。
     *
     * @param year 年份
     * @return 是否是闰年
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * 获取月份天数。
     *
     * @param year 年份
     * @param month 月份（1-12）
     * @return 天数
     */
    fun daysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * 倒计时格式。
     *
     * @param millis 毫秒数
     * @return 格式化后的倒计时字符串
     */
    fun formatCountdown(millis: Long): String {
        val seconds = millis / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(locale, "%02d:%02d:%02d", hours, minutes, secs)
    }

    /**
     * 时长格式化。
     *
     * @param millis 毫秒数
     * @return 格式化后的时长字符串
     */
    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        if (seconds < 60) return "${seconds}秒"
        val minutes = seconds / 60
        if (minutes < 60) return "${minutes}分钟"
        val hours = minutes / 60
        if (hours < 24) return "${hours}小时${minutes % 60}分钟"
        val days = hours / 24
        return "${days}天${hours % 24}小时"
    }
}
