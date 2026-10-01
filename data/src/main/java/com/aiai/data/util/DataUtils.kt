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
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * 数据工具类。
 *
 * 提供数据转换、格式化、验证等通用方法。
 */
object DataUtils {

    private const val TAG = "DataUtils"

    /**
     * 生成唯一ID。
     *
     * @return UUID字符串
     */
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 生成带前缀的唯一ID。
     *
     * @param prefix 前缀
     * @return 带前缀的ID
     */
    fun generateId(prefix: String): String {
        return "${prefix}_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(8)}"
    }

    /**
     * 当前时间戳（毫秒）。
     */
    fun now(): Long = System.currentTimeMillis()

    /**
     * 格式化时间戳为可读字符串。
     *
     * @param timestamp 时间戳
     * @param pattern 格式模式
     * @return 格式化字符串
     */
    fun formatTime(timestamp: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        return SimpleDateFormat(pattern, Locale.US).format(Date(timestamp))
    }

    /**
     * 将对象转换为JSON字符串。
     *
     * @param obj 对象
     * @return JSON字符串
     */
    fun toJson(obj: Any): String {
        return try {
            when (obj) {
                is Map<*, *> -> JSONObject(obj as Map<*, *>).toString()
                is List<*> -> JSONArray(obj).toString()
                else -> JSONObject().put("value", obj).toString()
            }
        } catch (e: Exception) {
            Log.w(TAG, "toJson failed: ${e.message}")
            "{}"
        }
    }

    /**
     * 从JSON字符串解析Map。
     *
     * @param json JSON字符串
     * @return Map
     */
    fun jsonToMap(json: String): Map<String, Any> {
        return try {
            JSONObject(json).toMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    /**
     * JSONObject扩展函数转Map。
     */
    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = when (val value = get(key)) {
                is JSONObject -> value.toMap()
                is JSONArray -> value.toList()
                else -> value
            }
        }
        return map
    }

    /**
     * JSONArray扩展函数转List。
     */
    private fun JSONArray.toList(): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until length()) {
            list.add(when (val value = get(i)) {
                is JSONObject -> value.toMap()
                is JSONArray -> value.toList()
                else -> value
            })
        }
        return list
    }

    /**
     * 安全地执行块，捕获异常。
     *
     * @param block 执行块
     * @return 结果或null
     */
    fun <T> safe(block: () -> T): T? {
        return try {
            block()
        } catch (e: Exception) {
            Log.w(TAG, "safe block failed: ${e.message}")
            null
        }
    }

    /**
     * 计算文本的token估算数。
     *
     * @param text 文本
     * @return 估算token数
     */
    fun estimateTokens(text: String): Int {
        // 粗略估算：英文约4字符=1token，中文约1.5字符=1token
        val chineseChars = text.count { it.code in 0x4E00..0x9FFF }
        val otherChars = text.length - chineseChars
        return (chineseChars / 1.5 + otherChars / 4.0).toInt()
    }

    /**
     * 格式化数字为简短形式。
     *
     * @param num 数字
     * @return 格式化字符串（如1.2K, 3.5M）
     */
    fun formatNumber(num: Long): String {
        return when {
            num < 1000 -> num.toString()
            num < 1_000_000 -> String.format("%.1fK", num / 1000.0)
            num < 1_000_000_000 -> String.format("%.1fM", num / 1_000_000.0)
            else -> String.format("%.1fB", num / 1_000_000_000.0)
        }
    }

    /**
     * 截断文本到指定长度。
     *
     * @param text 原文
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) text
        else text.take(maxLength) + "…"
    }
}
