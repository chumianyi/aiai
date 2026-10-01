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

import java.text.DecimalFormat

/**
 * 数字格式化工具类。
 */
object NumberFormatUtil {

    /** 千分位格式化。 */
    fun formatThousands(number: Long): String {
        return DecimalFormat("#,##0").format(number)
    }

    /** 保留两位小数。 */
    fun formatDecimal(number: Double): String {
        return DecimalFormat("#,##0.00").format(number)
    }

    /** 百分比格式化。 */
    fun formatPercent(value: Double): String {
        return DecimalFormat("0.0%").format(value)
    }

    /** 文件大小格式化。 */
    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var index = 0
        while (size >= 1024 && index < units.size - 1) {
            size /= 1024
            index++
        }
        return String.format("%.1f %s", size, units[index])
    }

    /** 简短数字（1.2万 / 3.4亿）。 */
    fun formatShortNumber(number: Long): String {
        return when {
            number >= 100_000_000 -> String.format("%.1f亿", number / 100_000_000.0)
            number >= 10_000 -> String.format("%.1f万", number / 10_000.0)
            else -> number.toString()
        }
    }
}
