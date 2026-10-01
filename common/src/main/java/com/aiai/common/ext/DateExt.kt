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
package com.aiai.common.ext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 日期相关扩展函数集合。
 *
 * 提供格式化、相对时间、时区转换、日期计算、星座生肖、工作日判断等。
 */

// region 格式化

/** 默认日期格式：yyyy-MM-dd。 */
fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

/** 时间戳（Long）格式化为日期字符串。 */
fun Long.formatDate(pattern: String = "yyyy-MM-dd"): String {
    return Date(this).format(pattern)
}

/** 时间戳格式化为日期时间字符串。 */
fun Long.formatDateTime(): String = formatDate("yyyy-MM-dd HH:mm:ss")

/** 时间戳格式化为时间字符串（HH:mm）。 */
fun Long.formatTime(): String = formatDate("HH:mm")

/** 解析字符串为 Date（失败返回 null）。 */
fun String.parseDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}

// endregion

// region 相对时间

/**
 * 相对时间描述：刚刚 / x分钟前 / x小时前 / x天前。
 */
fun Long.toRelativeTime(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000 -> "刚刚"
        diff < 3_600_000 -> "${diff / 60_000}分钟前"
        diff < 86_400_000 -> "${diff / 3_600_000}小时前"
        diff < 7 * 86_400_000 -> "${diff / 86_400_000}天前"
        else -> formatDate("yyyy-MM-dd")
    }
}

/** 未来时间相对描述。 */
fun Long.toFutureRelative(): String {
    val diff = this - System.currentTimeMillis()
    return when {
        diff < 60_000 -> "即将开始"
        diff < 3_600_000 -> "${diff / 60_000}分钟后"
        diff < 86_400_000 -> "${diff / 3_600_000}小时后"
        else -> "${diff / 86_400_000}天后"
    }
}

// endregion

// region 日期计算

/** 加上 [days] 天。 */
fun Date.plusDays(days: Int): Date {
    val cal = Calendar.getInstance().apply { time = this@plusDays }
    cal.add(Calendar.DAY_OF_YEAR, days)
    return cal.time
}

/** 减去 [days] 天。 */
fun Date.minusDays(days: Int): Date = plusDays(-days)

/** 加上 [hours] 小时。 */
fun Date.plusHours(hours: Int): Date {
    val cal = Calendar.getInstance().apply { time = this@plusHours }
    cal.add(Calendar.HOUR_OF_DAY, hours)
    return cal.time
}

/** 两个日期相差天数。 */
fun Date.daysBetween(other: Date): Long {
    val diff = time - other.time
    return TimeUnit.MILLISECONDS.toDays(diff)
}

/** 是否为今天。 */
fun Date.isToday(): Boolean {
    val now = Calendar.getInstance()
    val cal = Calendar.getInstance().apply { time = this@isToday }
    return now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
        now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
}

/** 是否为明天。 */
fun Date.isTomorrow(): Boolean = plusDays(1).isToday()

/** 是否为闰年。 */
fun Date.isLeapYear(): Boolean {
    val year = Calendar.getInstance().apply { time = this@isLeapYear }.get(Calendar.YEAR)
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

// endregion

// region 星座 & 生肖

/** 获取星座。 */
fun Date.zodiacSign(): String {
    val cal = Calendar.getInstance().apply { time = this@zodiacSign }
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)
    return when (month) {
        1 -> if (day < 20) "摩羯座" else "水瓶座"
        2 -> if (day < 19) "水瓶座" else "双鱼座"
        3 -> if (day < 21) "双鱼座" else "白羊座"
        4 -> if (day < 20) "白羊座" else "金牛座"
        5 -> if (day < 21) "金牛座" else "双子座"
        6 -> if (day < 22) "双子座" else "巨蟹座"
        7 -> if (day < 23) "巨蟹座" else "狮子座"
        8 -> if (day < 23) "狮子座" else "处女座"
        9 -> if (day < 23) "处女座" else "天秤座"
        10 -> if (day < 24) "天秤座" else "天蝎座"
        11 -> if (day < 23) "天蝎座" else "射手座"
        else -> if (day < 22) "射手座" else "摩羯座"
    }
}

/** 获取生肖。 */
fun Date.zodiacAnimal(): String {
    val year = Calendar.getInstance().apply { time = this@zodiacAnimal }.get(Calendar.YEAR)
    val animals = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
    return animals[(year - 4) % 12]
}

// endregion

// region 工作日

/** 是否为工作日（周一到周五）。 */
fun Date.isWeekday(): Boolean {
    val cal = Calendar.getInstance().apply { time = this@isWeekday }
    val day = cal.get(Calendar.DAY_OF_WEEK)
    return day != Calendar.SATURDAY && day != Calendar.SUNDAY
}

/** 是否为周末。 */
fun Date.isWeekend(): Boolean = !isWeekday()

// endregion
