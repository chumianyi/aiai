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
package com.aiai.network.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 网络工具类集合。
 *
 * 提供网络相关的通用工具方法。
 */
object NetworkUtils {

    private const val TAG = "NetworkUtils"

    /**
     * 格式化JSON字符串，美化输出。
     *
     * @param json 原始JSON字符串
     * @return 格式化后的JSON
     */
    fun prettyPrintJson(json: String): String {
        return try {
            when {
                json.startsWith("{") -> JSONObject(json).toString(2)
                json.startsWith("[") -> JSONArray(json).toString(2)
                else -> json
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to pretty print JSON: ${e.message}")
            json
        }
    }

    /**
     * 格式化字节数为人类可读字符串。
     *
     * @param bytes 字节数
     * @return 格式化字符串（如1.5MB）
     */
    fun formatBytes(bytes: Long): String {
        if (bytes < 0) return "0 B"
        if (bytes < 1024) return "$bytes B"
        val kb = bytes / 1024.0
        if (kb < 1024) return String.format("%.1f KB", kb)
        val mb = kb / 1024.0
        if (mb < 1024) return String.format("%.1f MB", mb)
        val gb = mb / 1024.0
        return String.format("%.2f GB", gb)
    }

    /**
     * 格式化耗时为人类可读字符串。
     *
     * @param ms 毫秒数
     * @return 格式化字符串
     */
    fun formatDuration(ms: Long): String {
        return when {
            ms < 1000 -> "${ms}ms"
            ms < 60_000 -> String.format("%.1fs", ms / 1000.0)
            ms < 3_600_000 -> String.format("%.1fmin", ms / 60_000.0)
            else -> String.format("%.1fh", ms / 3_600_000.0)
        }
    }

    /**
     * 格式化时间戳为ISO8601字符串。
     *
     * @param timestamp 时间戳（毫秒）
     * @return ISO8601格式时间字符串
     */
    fun formatTimestampIso8601(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timestamp))
    }

    /**
     * 从URL中提取域名。
     *
     * @param url URL字符串
     * @return 域名
     */
    fun extractDomain(url: String): String {
        return try {
            val start = url.indexOf("://") + 3
            val end = url.indexOf('/', start)
            if (end == -1) url.substring(start) else url.substring(start, end)
        } catch (e: Exception) {
            url
        }
    }

    /**
     * 从URL中提取路径。
     *
     * @param url URL字符串
     * @return 路径
     */
    fun extractPath(url: String): String {
        return try {
            val start = url.indexOf("://") + 3
            val slashIndex = url.indexOf('/', start)
            if (slashIndex == -1) "/" else url.substring(slashIndex)
        } catch (e: Exception) {
            url
        }
    }

    /**
     * 检查是否为有效URL。
     *
     * @param url URL字符串
     * @return true如果有效
     */
    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    /**
     * 检查是否为WebSocket URL。
     *
     * @param url URL字符串
     * @return true如果是ws://或wss://
     */
    fun isWebSocketUrl(url: String): Boolean {
        return url.startsWith("ws://") || url.startsWith("wss://")
    }

    /**
     * 拼接URL路径。
     *
     * @param baseUrl 基础URL
     * @param path 路径
     * @return 完整URL
     */
    fun buildUrl(baseUrl: String, path: String): String {
        val base = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        val p = if (path.startsWith("/")) path.substring(1) else path
        return base + p
    }

    /**
     * 生成唯一请求ID。
     *
     * @return 请求ID
     */
    fun generateRequestId(): String {
        return "req_${System.currentTimeMillis()}_${(100000..999999).random()}"
    }

    /**
     * 计算文本的MD5哈希。
     *
     * @param text 输入文本
     * @return MD5哈希字符串
     */
    fun md5(text: String): String {
        val md = java.security.MessageDigest.getInstance("MD5")
        val digest = md.digest(text.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 计算文本的SHA256哈希。
     *
     * @param text 输入文本
     * @return SHA256哈希字符串
     */
    fun sha256(text: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(text.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    /**
     * 字符串安全截断。
     *
     * @param text 原始文本
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) text
        else text.substring(0, maxLength) + "..."
    }

    /**
     * 检查字符串是否为空或空白。
     *
     * @param str 字符串
     * @return true如果为空
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim().isEmpty()
    }

    /**
     * 安全地获取字符串默认值。
     *
     * @param str 原始字符串
     * @param default 默认值
     * @return 非空字符串
     */
    fun orDefault(str: String?, default: String = ""): String {
        return str ?: default
    }
}
