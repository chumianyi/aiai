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
import java.util.Date
import java.util.Locale

/**
 * 时间工具类。
 */
object TimeUtil {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    /** 当前时间字符串。 */
    fun now(): String = sdf.format(Date())

    /** 时间戳格式化。 */
    fun format(timestamp: Long): String = sdf.format(Date(timestamp))

    /** 时间戳格式化为日期。 */
    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
    }

    /** 时间戳格式化为时间。 */
    fun formatTime(timestamp: Long): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
    }

    /** 相对时间描述。 */
    fun getRelativeTime(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 7 * 86_400_000 -> "${diff / 86_400_000}天前"
            else -> formatDate(timestamp)
        }
    }

    /** 今天开始时间戳。 */
    fun todayStart(): Long {
        val sdfDay = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdfDay.parse(sdfDay.format(Date()))?.time ?: 0L
    }
}
