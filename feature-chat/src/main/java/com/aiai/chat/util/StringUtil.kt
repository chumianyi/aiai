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

import android.util.Patterns

/**
 * 字符串工具类
 *
 * 提供字符串处理、验证、格式化等功能。
 */
object StringUtil {

    /**
     * 是否为空或空白
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim().isEmpty()
    }

    /**
     * 是否非空
     */
    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    /**
     * 截断字符串到指定长度
     */
    fun truncate(str: String, maxLength: Int, ellipsis: String = "..."): String {
        return if (str.length <= maxLength) str
        else str.take(maxLength - ellipsis.length) + ellipsis
    }

    /**
     * 移除Markdown标记
     */
    fun stripMarkdown(markdown: String): String {
        return markdown
            .replace(Regex("```[\\s\\S]*?```"), "")
            .replace(Regex("`[^`]+`"), "")
            .replace(Regex("\\*\\*([^*]+)\\*\\*"), "$1")
            .replace(Regex("\\*([^*]+)\\*"), "$1")
            .replace(Regex("\\[([^]]+)\\]\\([^)]+\\)"), "$1")
            .replace(Regex("^#+\\s*"), "")
            .replace(Regex("^\\s*[-*+]\\s+"), "")
            .trim()
    }

    /**
     * 提取纯文本预览
     */
    fun getPreview(text: String, maxLength: Int = 100): String {
        val plain = stripMarkdown(text).replace(Regex("\\s+"), " ").trim()
        return truncate(plain, maxLength)
    }

    /**
     * 验证邮箱格式
     */
    fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * 验证URL格式
     */
    fun isUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    /**
     * 统计字数（中文按字算，英文按词算）
     */
    fun countWords(text: String): Int {
        val chineseChars = Regex("[\\u4e00-\\u9fa5]").findAll(text).count()
        val englishWords = Regex("[a-zA-Z]+").findAll(text).count()
        return chineseChars + englishWords
    }

    /**
     * 计算Token估算值（中文约1.5字/token，英文约0.75词/token）
     */
    fun estimateTokens(text: String): Int {
        val chineseChars = Regex("[\\u4e00-\\u9fa5]").findAll(text).count()
        val englishWords = Regex("[a-zA-Z]+").findAll(text).count()
        return (chineseChars * 1.5 + englishWords * 0.75).toInt()
    }

    /**
     * 驼峰命名转下划线
     */
    fun camelToSnake(camel: String): String {
        return camel.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
    }

    /**
     * 下划线转驼峰
     */
    fun snakeToCamel(snake: String): String {
        return snake.split("_").mapIndexed { index, part ->
            if (index == 0) part else part.replaceFirstChar { it.uppercase() }
        }.joinToString("")
    }
}
