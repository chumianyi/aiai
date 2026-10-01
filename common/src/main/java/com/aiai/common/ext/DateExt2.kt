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
package com.aiai.common.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * Date 扩展函数集（扩展版）。
 */

/** 格式化日期。 */
fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

/** 时间戳格式化。 */
fun Long.formatDate(pattern: String = "yyyy-MM-dd"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}

/** 相对时间描述。 */
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

/** 是否为今天。 */
fun Date.isToday(): Boolean {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(Date())
}

/** 是否为昨天。 */
fun Date.isYesterday(): Boolean {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(this) == sdf.format(cal.time)
}

/** 是否为今年。 */
fun Date.isThisYear(): Boolean {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
}

/** 加几天。 */
fun Date.addDays(days: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.DAY_OF_YEAR, days)
    return cal.time
}

/** 星期几（中文）。 */
fun Date.weekday(): String {
    val cal = Calendar.getInstance()
    cal.time = this
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
