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
package com.aiai.chat.ext

import android.content.Intent
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * String扩展函数集合
 */

/**
 * 字符串是否为有效邮箱
 */
fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * 字符串是否为有效URL
 */
fun String.isUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

/**
 * 字符串是否为空或空白
 */
fun String?.isNullOrBlank(): Boolean {
    return this == null || this.isBlank()
}

/**
 * 字符串截断
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (length <= maxLength) this
    else take(maxLength - ellipsis.length) + ellipsis
}

/**
 * 字符串首字母大写
 */
fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

/**
 * 驼峰转下划线
 */
fun String.camelToSnake(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
}

/**
 * 下划线转驼峰
 */
fun String.snakeToCamel(): String {
    return split("_").mapIndexed { index, part ->
        if (index == 0) part else part.capitalize()
    }.joinToString("")
}

/**
 * 移除Markdown格式
 */
fun String.stripMarkdown(): String {
    return this
        .replace(Regex("```[\\s\\S]*?```"), "")
        .replace(Regex("`[^`]+`"), "")
        .replace(Regex("\\*\\*([^*]+)\\*\\*"), "$1")
        .replace(Regex("\\*([^*]+)\\*"), "$1")
        .replace(Regex("\\[([^]]+)\\]\\([^)]+\\)"), "$1")
        .replace(Regex("^#+\\s*"), "")
        .trim()
}

/**
 * 获取文本预览
 */
fun String.getPreview(maxLength: Int = 100): String {
    val plain = stripMarkdown().replace(Regex("\\s+"), " ").trim()
    return plain.truncate(maxLength)
}

/**
 * 估算Token数
 */
fun String.estimateTokens(): Int {
    val chineseChars = Regex("[\\u4e00-\\u9fa5]").findAll(this).count()
    val englishWords = Regex("[a-zA-Z]+").findAll(this).count()
    return (chineseChars * 1.5 + englishWords * 0.75).toInt()
}
