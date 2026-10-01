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
package com.aiai.common.util.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 时间格式化工具类。
 *
 * 提供常用时间格式的格式化与解析。
 */
object TimeFormatUtil {

    const val PATTERN_FULL = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE = "yyyy-MM-dd"
    const val PATTERN_TIME = "HH:mm:ss"
    const val PATTERN_YEAR_MONTH = "yyyy-MM"
    const val PATTERN_MONTH_DAY = "MM-dd"
    const val PATTERN_CHINESE_FULL = "yyyy年MM月dd日 HH时mm分"
    const val PATTERN_CHINESE_DATE = "yyyy年MM月dd日"
    const val PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val PATTERN_FILE = "yyyyMMdd_HHmmss"

    /** 格式化当前时间。 */
    fun now(pattern: String = PATTERN_FULL): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
    }

    /** 格式化时间戳。 */
    fun format(timestamp: Long, pattern: String = PATTERN_FULL): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
    }

    /** 解析字符串为时间戳。 */
    fun parse(timeStr: String, pattern: String = PATTERN_FULL): Long? {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).parse(timeStr)?.time
        } catch (e: Exception) {
            null
        }
    }

    /** 智能格式化：今天显示时间，昨天显示"昨天"，一周内显示星期，更早显示日期。 */
    fun smartFormat(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            isSameDay(timestamp, now) -> format(timestamp, "HH:mm")
            isYesterday(timestamp) -> "昨天 ${format(timestamp, "HH:mm")}"
            isWithinWeek(timestamp) -> format(timestamp, "EEEE HH:mm")
            else -> format(timestamp, "yyyy-MM-dd")
        }
    }

    private fun isSameDay(a: Long, b: Long): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date(a)) == sdf.format(Date(b))
    }

    private fun isYesterday(ts: Long): Boolean {
        return isSameDay(ts, System.currentTimeMillis() - 86_400_000)
    }

    private fun isWithinWeek(ts: Long): Boolean {
        return System.currentTimeMillis() - ts < 7 * 86_400_000
    }
}
