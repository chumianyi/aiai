/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 日期时间工具类
 *
 * 提供各种日期格式化、时间差计算、相对时间显示等功能。
 */
object DateUtil {

    const val FORMAT_FULL = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val FORMAT_TIME = "HH:mm"
    const val FORMAT_MONTH_DAY = "MM-dd"
    const val FORMAT_YEAR_MONTH_DAY = "yyyy年MM月dd日"
    const val FORMAT_CHAT_TIME = "MM-dd HH:mm"
    const val FORMAT_MESSAGE_TIME = "HH:mm"

    /**
     * 时间戳转格式化字符串
     */
    fun formatTimestamp(timestamp: Long, pattern: String = FORMAT_FULL): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
    }

    /**
     * 聊天列表时间显示
     * - 今天：显示 HH:mm
     * - 昨天：显示 昨天 HH:mm
     * - 本周：显示 星期X
     * - 今年：显示 MM-dd
     * - 更早：显示 yyyy-MM-dd
     */
    fun formatChatListTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            isSameDay(timestamp, now) -> {
                formatTimestamp(timestamp, FORMAT_MESSAGE_TIME)
            }
            isYesterday(timestamp, now) -> {
                "昨天 ${formatTimestamp(timestamp, FORMAT_MESSAGE_TIME)}"
            }
            isWithinWeek(timestamp, now) -> {
                getDayOfWeek(timestamp)
            }
            isSameYear(timestamp, now) -> {
                formatTimestamp(timestamp, FORMAT_MONTH_DAY)
            }
            else -> {
                formatTimestamp(timestamp, FORMAT_DATE)
            }
        }
    }

    /**
     * 相对时间描述（如"3分钟前"）
     */
    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 2 * 86_400_000 -> "昨天"
            diff < 7 * 86_400_000 -> "${diff / 86_400_000}天前"
            diff < 30 * 86_400_000 -> "${diff / (7 * 86_400_000)}周前"
            else -> formatTimestamp(timestamp, FORMAT_DATE)
        }
    }

    /**
     * 消息分组时间标签
     */
    fun getTimeGroupLabel(timestamp: Long): String {
        val now = System.currentTimeMillis()
        return when {
            isSameDay(timestamp, now) -> "今天"
            isYesterday(timestamp, now) -> "昨天"
            isWithinWeek(timestamp, now) -> getDayOfWeek(timestamp)
            isSameYear(timestamp, now) -> formatTimestamp(timestamp, FORMAT_MONTH_DAY)
            else -> formatTimestamp(timestamp, FORMAT_DATE)
        }
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date(t1)) == sdf.format(Date(t2))
    }

    private fun isYesterday(timestamp: Long, now: Long): Boolean {
        val yesterday = now - 86_400_000
        return isSameDay(timestamp, yesterday)
    }

    private fun isWithinWeek(timestamp: Long, now: Long): Boolean {
        return (now - timestamp) < 7 * 86_400_000
    }

    private fun isSameYear(t1: Long, t2: Long): Boolean {
        val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
        return sdf.format(Date(t1)) == sdf.format(Date(t2))
    }

    private fun getDayOfWeek(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply { time = Date(timestamp) }
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
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
     * 计算两个时间戳之间的天数
     */
    fun getDaysBetween(start: Long, end: Long): Long {
        val diff = end - start
        return TimeUnit.MILLISECONDS.toDays(diff)
    }

    /**
     * 格式化时长（毫秒转 mm:ss）
     */
    fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * 获取今天开始的时间戳
     */
    fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * 获取本周开始的时间戳
     */
    fun getWeekStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * 获取本月开始的时间戳
     */
    fun getMonthStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }
}
