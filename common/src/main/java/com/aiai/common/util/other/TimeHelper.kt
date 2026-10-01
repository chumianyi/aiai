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

import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间日期工具。
 */
object TimeHelper {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun now(): String = sdf.format(Date())
    fun format(ts: Long): String = sdf.format(Date(ts))
    fun formatDate(ts: Long): String = sdfDate.format(Date(ts))

    fun relative(ts: Long): String {
        val diff = System.currentTimeMillis() - ts
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 7 * 86_400_000 -> "${diff / 86_400_000}天前"
            else -> formatDate(ts)
        }
    }

    fun isToday(ts: Long): Boolean = sdfDate.format(Date(ts)) == sdfDate.format(Date())

    fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
