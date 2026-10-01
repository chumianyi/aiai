/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.util

import android.util.Log

/**
 * 文本处理工具。
 *
 * 提供文本分析、格式化等功能。
 */
object TextProcessor {

    private const val TAG = "TextProcessor"

    /**
     * 统计文本中的中文字符数。
     */
    fun countChineseChars(text: String): Int {
        return text.count { it.code in 0x4E00..0x9FFF }
    }

    /**
     * 统计文本中的英文单词数。
     */
    fun countEnglishWords(text: String): Int {
        return text.split(Regex("\\s+")).filter { it.isNotBlank() }.size
    }

    /**
     * 估算文本阅读时间（秒）。
     */
    fun estimateReadTimeSec(text: String): Int {
        val chineseChars = countChineseChars(text)
        val englishWords = countEnglishWords(text)
        // 中文阅读速度约300字/分钟，英文约200词/分钟
        return ((chineseChars / 5.0) + (englishWords / 3.3)).toInt()
    }

    /**
     * 格式化阅读时间。
     */
    fun formatReadTime(seconds: Int): String {
        return when {
            seconds < 60 -> "${seconds}秒"
            seconds < 3600 -> "${seconds / 60}分钟"
            else -> "${seconds / 3600}小时${(seconds % 3600) / 60}分钟"
        }
    }

    /**
     * 提取文本中的URL。
     */
    fun extractUrls(text: String): List<String> {
        val regex = Regex("https?://[\\w\\d.-]+[/\\w\\d.?&=%-]*")
        return regex.findAll(text).map { it.value }.toList()
    }

    /**
     * 提取文本中的@提及。
     */
    fun extractMentions(text: String): List<String> {
        val regex = Regex("@(\\w+)")
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }

    /**
     * 提取文本中的#标签。
     */
    fun extractHashtags(text: String): List<String> {
        val regex = Regex("#([\\w\\u4e00-\\u9fff]+)")
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }

    /**
     * 清理文本中的多余空白字符。
     */
    fun normalizeWhitespace(text: String): String {
        return text.trim()
            .replace(Regex("\\n{3,}"), "\n\n")
            .replace(Regex(" {2,}"), " ")
    }

    /**
     * 转义HTML特殊字符。
     */
    fun escapeHtml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

    /**
     * 计算文本哈希值。
     */
    fun hashCode(text: String): Int {
        return text.hashCode()
    }
}
