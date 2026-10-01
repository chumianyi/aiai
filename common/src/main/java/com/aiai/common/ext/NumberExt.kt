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

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

/**
 * 数字相关扩展函数集合。
 *
 * 提供千分位格式化、百分比、字节格式化、范围约束等能力。
 */

// region 格式化

/** 千分位格式化（默认保留 2 位小数）。 */
fun Number.thousands(decimals: Int = 2): String {
    val nf = NumberFormat.getNumberInstance(Locale.getDefault())
    nf.maximumFractionDigits = decimals
    nf.minimumFractionDigits = decimals
    return nf.format(this)
}

/** 百分比格式化（乘以 100 后加 %）。 */
fun Number.percent(decimals: Int = 1): String {
    val pf = NumberFormat.getPercentInstance(Locale.getDefault())
    pf.maximumFractionDigits = decimals
    return pf.format(this)
}

/** 金额格式化：¥1,234.56。 */
fun Number.money(): String = "¥" + thousands(2)

/** 中文大写金额（简化版）。 */
fun Number.chineseMoney(): String {
    val digits = "零壹贰叁肆伍陆柒捌玖"
    val units = arrayOf("", "拾", "佰", "仟")
    val bigUnits = arrayOf("", "万", "亿")
    var value = this.toLong()
    if (value == 0L) return "零元整"
    val sb = StringBuilder()
    var unitIdx = 0
    while (value > 0) {
        val section = (value % 10000).toInt()
        if (section > 0) {
            var secStr = ""
            var s = section
            var u = 0
            while (s > 0) {
                val d = s % 10
                if (d != 0) secStr = digits[d] + units[u] + secStr
                else if (secStr.isNotEmpty() && !secStr.startsWith("零")) secStr = "零$secStr"
                s /= 10
                u++
            }
            sb.insert(0, secStr + bigUnits[unitIdx])
        }
        value /= 10000
        unitIdx++
    }
    return sb.toString() + "元整"
}

/** 字节大小格式化为易读字符串。 */
fun Long.readableBytes(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var s = this.toDouble()
    var i = 0
    while (s >= 1024 && i < units.size - 1) {
        s /= 1024
        i++
    }
    return DecimalFormat("#,##0.#").format(s) + " " + units[i]
}

/** Int 字节大小格式化。 */
fun Int.readableBytes(): String = toLong().readableBytes()

// endregion

// region 范围约束

/** 将数值限制在 [min, max] 区间。 */
fun Number.clamp(min: Number, max: Number): Double {
    val v = this.toDouble()
    return when {
        v < min.toDouble() -> min.toDouble()
        v > max.toDouble() -> max.toDouble()
        else -> v
    }
}

/** Int 限制在 0..[max]。 */
fun Int.coerceAtLeastZero(max: Int): Int = coerceIn(0, max)

/** 安全转 Int（失败返回默认值）。 */
fun String?.toIntSafe(default: Int = 0): Int {
    return this?.toIntOrNull() ?: default
}

/** 安全转 Long。 */
fun String?.toLongSafe(default: Long = 0L): Long {
    return this?.toLongOrNull() ?: default
}

/** 安全转 Double。 */
fun String?.toDoubleSafe(default: Double = 0.0): Double {
    return this?.toDoubleOrNull() ?: default
}

// endregion
