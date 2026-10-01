/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 时间格式化工具：相对时间、绝对时间、时长。
 */
object TimeUtils {

    private val fmtDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val fmtDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val fmtTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val fmtFull = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun formatDateTime(time: Long): String = fmtDateTime.format(Date(time))
    fun formatDate(time: Long): String = fmtDate.format(Date(time))
    fun formatTime(time: Long): String = fmtTime.format(Date(time))
    fun formatFull(time: Long): String = fmtFull.format(Date(time))

    /** 相对时间：刚刚 / n 分钟前 / n 小时前 / n 天前。 */
    fun relative(time: Long): String {
        val diff = System.currentTimeMillis() - time
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000} 分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000} 小时前"
            diff < 7 * 86_400_000 -> "${diff / 86_400_000} 天前"
            else -> formatDate(time)
        }
    }

    /** 时长格式化：将毫秒转为 mm:ss 或 hh:mm:ss。 */
    fun duration(ms: Long): String {
        val hrs = TimeUnit.MILLISECONDS.toHours(ms)
        val min = TimeUnit.MILLISECONDS.toMinutes(ms) % 60
        val sec = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        return if (hrs > 0) String.format("%d:%02d:%02d", hrs, min, sec)
        else String.format("%02d:%02d", min, sec)
    }

    /** 是否为今天。 */
    fun isToday(time: Long): Boolean {
        val now = Calendar.getInstance()
        val then = Calendar.getInstance().apply { time = Date(time) }
        return now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)
    }

    /** 是否为今年。 */
    fun isThisYear(time: Long): Boolean {
        val now = Calendar.getInstance()
        val then = Calendar.getInstance().apply { time = Date(time) }
        return now.get(Calendar.YEAR) == then.get(Calendar.YEAR)
    }
}
