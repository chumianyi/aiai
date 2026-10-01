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

import java.util.concurrent.TimeUnit

/**
 * 相对时间描述工具类。
 *
 * 将时间戳转换为"刚刚/x分钟前/x小时前"等人类可读描述。
 */
object TimeAgoUtil {

    /**
     * 将时间戳转换为相对时间描述。
     *
     * @param timestamp 目标时间戳（毫秒）
     * @param isFuture 是否为未来时间（倒计时）
     */
    fun format(timestamp: Long, isFuture: Boolean = false): String {
        val now = System.currentTimeMillis()
        val diff = if (isFuture) timestamp - now else now - timestamp
        if (diff < 0) return if (isFuture) "已过期" else "未来时间"

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        val suffix = if (isFuture) "后" else "前"
        return when {
            seconds < 1 -> "刚刚"
            seconds < 60 -> "${seconds}秒$suffix"
            minutes < 60 -> "${minutes}分钟$suffix"
            hours < 24 -> "${hours}小时$suffix"
            days < 7 -> "${days}天$suffix"
            days < 30 -> "${days / 7}周$suffix"
            days < 365 -> "${days / 30}个月$suffix"
            else -> "${days / 365}年$suffix"
        }
    }
}
