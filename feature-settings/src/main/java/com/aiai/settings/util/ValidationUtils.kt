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

import android.util.Patterns

/**
 * 输入校验工具：URL、邮箱、密钥格式等。
 */
object ValidationUtils {

    /** 校验接口地址：必须以 http(s):// 开头，且 host 合法。 */
    fun isValidBaseUrl(url: String): Boolean {
        if (url.isBlank()) return false
        if (!url.startsWith("http://") && !url.startsWith("https://")) return false
        return Patterns.WEB_URL.matcher(url.removePrefix("http://").removePrefix("https://")).matches()
    }

    /** 校验 API Key 非空且长度 >= 8。 */
    fun isValidApiKey(key: String): Boolean {
        return key.length >= 8
    }

    /** 校验模型名称非空。 */
    fun isValidModelName(name: String): Boolean {
        return name.isNotBlank() && name.length <= 64
    }

    /** 校验邮箱。 */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /** 校验手机号（中国大陆）。 */
    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    /** 计算字符串相似度（Levenshtein），用于模糊搜索。 */
    fun levenshtein(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j
        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[a.length][b.length]
    }

    /** 模糊匹配：query 是否命中 text（包含或编辑距离 <= 2）。 */
    fun fuzzyMatch(query: String, text: String): Boolean {
        val q = query.lowercase()
        val t = text.lowercase()
        if (t.contains(q)) return true
        if (q.length <= 3) return false
        return t.split(" ").any { levenshtein(q, it) <= 2 }
    }
}
