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
package com.aiai.common.util

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON 工具类。
 *
 * 提供 JSON 解析、构建、格式化、合并等功能。
 */
object JsonUtil {

    private const val INDENT_SPACES = 2

    /**
     * 解析 JSON 字符串为 JSONObject。
     *
     * @param json JSON 字符串
     * @return 解析后的 JSONObject，解析失败返回 null
     */
    fun parseObject(json: String?): JSONObject? {
        return try {
            if (json.isNullOrBlank()) null
            else JSONObject(json)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 解析 JSON 字符串为 JSONArray。
     *
     * @param json JSON 字符串
     * @return 解析后的 JSONArray，解析失败返回 null
     */
    fun parseArray(json: String?): JSONArray? {
        return try {
            if (json.isNullOrBlank()) null
            else JSONArray(json)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 格式化 JSON 字符串。
     *
     * @param json JSON 字符串
     * @return 格式化后的 JSON 字符串
     */
    fun format(json: String?): String {
        if (json.isNullOrBlank()) return ""
        return try {
            when {
                json.trimStart().startsWith("{") -> {
                    JSONObject(json).toString(INDENT_SPACES)
                }
                json.trimStart().startsWith("[") -> {
                    JSONArray(json).toString(INDENT_SPACES)
                }
                else -> json
            }
        } catch (e: Exception) {
            json
        }
    }

    /**
     * 从 JSONObject 中获取字符串值。
     *
     * @param json JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 字符串值
     */
    fun getString(json: JSONObject?, key: String, defaultValue: String = ""): String {
        return try {
            json?.optString(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从 JSONObject 中获取整数值。
     *
     * @param json JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 整数值
     */
    fun getInt(json: JSONObject?, key: String, defaultValue: Int = 0): Int {
        return try {
            json?.optInt(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从 JSONObject 中获取长整数值。
     *
     * @param json JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 长整数值
     */
    fun getLong(json: JSONObject?, key: String, defaultValue: Long = 0L): Long {
        return try {
            json?.optLong(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从 JSONObject 中获取布尔值。
     *
     * @param json JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    fun getBoolean(json: JSONObject?, key: String, defaultValue: Boolean = false): Boolean {
        return try {
            json?.optBoolean(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从 JSONObject 中获取双精度浮点值。
     *
     * @param json JSONObject
     * @param key 键名
     * @param defaultValue 默认值
     * @return 双精度浮点值
     */
    fun getDouble(json: JSONObject?, key: String, defaultValue: Double = 0.0): Double {
        return try {
            json?.optDouble(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从 JSONObject 中获取嵌套 JSONObject。
     *
     * @param json JSONObject
     * @param key 键名
     * @return 嵌套 JSONObject，不存在返回 null
     */
    fun getObject(json: JSONObject?, key: String): JSONObject? {
        return try {
            json?.optJSONObject(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 从 JSONObject 中获取嵌套 JSONArray。
     *
     * @param json JSONObject
     * @param key 键名
     * @return 嵌套 JSONArray，不存在返回 null
     */
    fun getArray(json: JSONObject?, key: String): JSONArray? {
        return try {
            json?.optJSONArray(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 构建 JSONObject。
     *
     * @param pairs 键值对
     * @return 构建的 JSONObject
     */
    fun buildObject(vararg pairs: Pair<String, Any?>): JSONObject {
        val json = JSONObject()
        pairs.forEach { (key, value) ->
            when (value) {
                null -> json.put(key, JSONObject.NULL)
                is Boolean -> json.put(key, value)
                is Int -> json.put(key, value)
                is Long -> json.put(key, value)
                is Double -> json.put(key, value)
                is String -> json.put(key, value)
                is JSONObject -> json.put(key, value)
                is JSONArray -> json.put(key, value)
                else -> json.put(key, value.toString())
            }
        }
        return json
    }

    /**
     * 构建 JSONArray。
     *
     * @param items 数组元素
     * @return 构建的 JSONArray
     */
    fun buildArray(vararg items: Any?): JSONArray {
        val array = JSONArray()
        items.forEach { item ->
            when (item) {
                null -> array.put(JSONObject.NULL)
                is Boolean -> array.put(item)
                is Int -> array.put(item)
                is Long -> array.put(item)
                is Double -> array.put(item)
                is String -> array.put(item)
                is JSONObject -> array.put(item)
                is JSONArray -> array.put(item)
                else -> array.put(item.toString())
            }
        }
        return array
    }

    /**
     * 合并两个 JSONObject。
     *
     * @param target 目标 JSONObject
     * @param source 源 JSONObject
     * @param overwrite 是否覆盖已有值
     * @return 合并后的 JSONObject
     */
    fun merge(target: JSONObject, source: JSONObject, overwrite: Boolean = true): JSONObject {
        val result = JSONObject(target.toString())
        val keys = source.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            if (overwrite || !result.has(key)) {
                result.put(key, source.get(key))
            }
        }
        return result
    }

    /**
     * 遍历 JSONObject。
     *
     * @param json JSONObject
     * @param action 遍历回调
     */
    fun forEach(json: JSONObject?, action: (String, Any?) -> Unit) {
        if (json == null) return
        val keys = json.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = if (json.isNull(key)) null else json.get(key)
            action(key, value)
        }
    }

    /**
     * 遍历 JSONArray。
     *
     * @param array JSONArray
     * @param action 遍历回调
     */
    fun forEach(array: JSONArray?, action: (Int, Any?) -> Unit) {
        if (array == null) return
        for (i in 0 until array.length()) {
            val value = if (array.isNull(i)) null else array.get(i)
            action(i, value)
        }
    }

    /**
     * 检查是否为有效 JSON。
     *
     * @param json JSON 字符串
     * @return 是否有效
     */
    fun isValid(json: String?): Boolean {
        if (json.isNullOrBlank()) return false
        return try {
            when {
                json.trimStart().startsWith("{") -> {
                    JSONObject(json)
                    true
                }
                json.trimStart().startsWith("[") -> {
                    JSONArray(json)
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 将 Map 转换为 JSONObject。
     *
     * @param map Map
     * @return JSONObject
     */
    fun fromMap(map: Map<String, Any?>): JSONObject {
        val json = JSONObject()
        map.forEach { (key, value) ->
            when (value) {
                null -> json.put(key, JSONObject.NULL)
                is Boolean -> json.put(key, value)
                is Int -> json.put(key, value)
                is Long -> json.put(key, value)
                is Double -> json.put(key, value)
                is String -> json.put(key, value)
                is Map<*, *> -> json.put(key, fromMap(value as Map<String, Any?>))
                is List<*> -> json.put(key, fromList(value))
                else -> json.put(key, value.toString())
            }
        }
        return json
    }

    /**
     * 将 List 转换为 JSONArray。
     *
     * @param list List
     * @return JSONArray
     */
    fun fromList(list: List<Any?>): JSONArray {
        val array = JSONArray()
        list.forEach { item ->
            when (item) {
                null -> array.put(JSONObject.NULL)
                is Boolean -> array.put(item)
                is Int -> array.put(item)
                is Long -> array.put(item)
                is Double -> array.put(item)
                is String -> array.put(item)
                is Map<*, *> -> array.put(fromMap(item as Map<String, Any?>))
                is List<*> -> array.put(fromList(item))
                else -> array.put(item.toString())
            }
        }
        return array
    }

    /**
     * 将 JSONObject 转换为 Map。
     *
     * @param json JSONObject
     * @return Map
     */
    fun toMap(json: JSONObject?): Map<String, Any?> {
        if (json == null) return emptyMap()
        val map = mutableMapOf<String, Any?>()
        val keys = json.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = when {
                json.isNull(key) -> null
                json.get(key) is JSONObject -> toMap(json.getJSONObject(key))
                json.get(key) is JSONArray -> toList(json.getJSONArray(key))
                else -> json.get(key)
            }
            map[key] = value
        }
        return map
    }

    /**
     * 将 JSONArray 转换为 List。
     *
     * @param array JSONArray
     * @return List
     */
    fun toList(array: JSONArray?): List<Any?> {
        if (array == null) return emptyList()
        val list = mutableListOf<Any?>()
        for (i in 0 until array.length()) {
            val value = when {
                array.isNull(i) -> null
                array.get(i) is JSONObject -> toMap(array.getJSONObject(i))
                array.get(i) is JSONArray -> toList(array.getJSONArray(i))
                else -> array.get(i)
            }
            list.add(value)
        }
        return list
    }
}
