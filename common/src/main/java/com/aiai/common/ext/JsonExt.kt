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

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON 扩展函数集合。
 *
 * 提供解析、构建、遍历、格式化、合并等常用 JSON 操作。
 */

// region 解析扩展

/**
 * 安全地获取字符串值。
 *
 * @param key 键名
 * @param defaultValue 默认值
 * @return 字符串值
 */
fun JSONObject.optStringSafe(key: String, defaultValue: String = ""): String {
    return if (has(key) && !isNull(key)) {
        optString(key, defaultValue)
    } else {
        defaultValue
    }
}

/**
 * 安全地获取 Int 值。
 *
 * @param key 键名
 * @param defaultValue 默认值
 * @return Int 值
 */
fun JSONObject.optIntSafe(key: String, defaultValue: Int = 0): Int {
    return if (has(key) && !isNull(key)) {
        optInt(key, defaultValue)
    } else {
        defaultValue
    }
}

/**
 * 安全地获取 Long 值。
 *
 * @param key 键名
 * @param defaultValue 默认值
 * @return Long 值
 */
fun JSONObject.optLongSafe(key: String, defaultValue: Long = 0L): Long {
    return if (has(key) && !isNull(key)) {
        optLong(key, defaultValue)
    } else {
        defaultValue
    }
}

/**
 * 安全地获取 Double 值。
 *
 * @param key 键名
 * @param defaultValue 默认值
 * @return Double 值
 */
fun JSONObject.optDoubleSafe(key: String, defaultValue: Double = 0.0): Double {
    return if (has(key) && !isNull(key)) {
        optDouble(key, defaultValue)
    } else {
        defaultValue
    }
}

/**
 * 安全地获取 Boolean 值。
 *
 * @param key 键名
 * @param defaultValue 默认值
 * @return Boolean 值
 */
fun JSONObject.optBooleanSafe(key: String, defaultValue: Boolean = false): Boolean {
    return if (has(key) && !isNull(key)) {
        optBoolean(key, defaultValue)
    } else {
        defaultValue
    }
}

/**
 * 安全地获取 JSONObject。
 *
 * @param key 键名
 * @return JSONObject，不存在返回空对象
 */
fun JSONObject.optJsonObjectSafe(key: String): JSONObject {
    return if (has(key) && !isNull(key)) {
        optJSONObject(key) ?: JSONObject()
    } else {
        JSONObject()
    }
}

/**
 * 安全地获取 JSONArray。
 *
 * @param key 键名
 * @return JSONArray，不存在返回空数组
 */
fun JSONObject.optJsonArraySafe(key: String): JSONArray {
    return if (has(key) && !isNull(key)) {
        optJSONArray(key) ?: JSONArray()
    } else {
        JSONArray()
    }
}

// endregion

// region 构建扩展

/**
 * 构建 JSONObject DSL。
 *
 * @param block 构建器
 * @return JSONObject
 */
fun jsonObject(block: JSONObjectBuilder.() -> Unit): JSONObject {
    val builder = JSONObjectBuilder()
    builder.block()
    return builder.build()
}

/**
 * JSONObject 构建器。
 */
class JSONObjectBuilder {
    private val map = mutableMapOf<String, Any?>()

    /** 添加键值对。 */
    fun put(key: String, value: Any?) {
        map[key] = value
    }

    /** 添加字符串。 */
    fun putString(key: String, value: String?) {
        map[key] = value ?: JSONObject.NULL
    }

    /** 添加 Int。 */
    fun putInt(key: String, value: Int?) {
        map[key] = value ?: JSONObject.NULL
    }

    /** 添加 Long。 */
    fun putLong(key: String, value: Long?) {
        map[key] = value ?: JSONObject.NULL
    }

    /** 添加 Double。 */
    fun putDouble(key: String, value: Double?) {
        map[key] = value ?: JSONObject.NULL
    }

    /** 添加 Boolean。 */
    fun putBoolean(key: String, value: Boolean?) {
        map[key] = value ?: JSONObject.NULL
    }

    /** 添加嵌套对象。 */
    fun putJsonObject(key: String, block: JSONObjectBuilder.() -> Unit) {
        val nestedBuilder = JSONObjectBuilder()
        nestedBuilder.block()
        map[key] = nestedBuilder.build()
    }

    /** 添加数组。 */
    fun putJsonArray(key: String, block: JSONArrayBuilder.() -> Unit) {
        val arrayBuilder = JSONArrayBuilder()
        arrayBuilder.block()
        map[key] = arrayBuilder.build()
    }

    /** 构建 JSONObject。 */
    fun build(): JSONObject {
        val json = JSONObject()
        map.forEach { (key, value) ->
            when (value) {
                is JSONObject -> json.put(key, value)
                is JSONArray -> json.put(key, value)
                is String -> json.put(key, value)
                is Int -> json.put(key, value)
                is Long -> json.put(key, value)
                is Double -> json.put(key, value)
                is Boolean -> json.put(key, value)
                null, JSONObject.NULL -> json.put(key, JSONObject.NULL)
                else -> json.put(key, value.toString())
            }
        }
        return json
    }
}

/**
 * JSONArray 构建器。
 */
class JSONArrayBuilder {
    private val list = mutableListOf<Any?>()

    /** 添加字符串。 */
    fun add(value: String?) {
        list.add(value ?: JSONObject.NULL)
    }

    /** 添加 Int。 */
    fun add(value: Int?) {
        list.add(value ?: JSONObject.NULL)
    }

    /** 添加 Long。 */
    fun add(value: Long?) {
        list.add(value ?: JSONObject.NULL)
    }

    /** 添加 Double。 */
    fun add(value: Double?) {
        list.add(value ?: JSONObject.NULL)
    }

    /** 添加 Boolean。 */
    fun add(value: Boolean?) {
        list.add(value ?: JSONObject.NULL)
    }

    /** 添加嵌套对象。 */
    fun addObject(block: JSONObjectBuilder.() -> Unit) {
        val builder = JSONObjectBuilder()
        builder.block()
        list.add(builder.build())
    }

    /** 构建 JSONArray。 */
    fun build(): JSONArray {
        val array = JSONArray()
        list.forEach { item ->
            when (item) {
                is JSONObject -> array.put(item)
                is JSONArray -> array.put(item)
                is String -> array.put(item)
                is Int -> array.put(item)
                is Long -> array.put(item)
                is Double -> array.put(item)
                is Boolean -> array.put(item)
                null, JSONObject.NULL -> array.put(JSONObject.NULL)
                else -> array.put(item.toString())
            }
        }
        return array
    }
}

// endregion

// region 遍历

/**
 * 遍历 JSONObject 所有键值对。
 *
 * @param action 对每个键值对执行的操作
 */
fun JSONObject.forEach(action: (String, Any?) -> Unit) {
    val keys = keys()
    while (keys.hasNext()) {
        val key = keys.next()
        val value = when {
            isNull(key) -> null
            else -> opt(key)
        }
        action(key, value)
    }
}

/**
 * 遍历 JSONArray 所有元素。
 *
 * @param action 对每个元素执行的操作
 */
fun JSONArray.forEach(action: (Any?) -> Unit) {
    for (i in 0 until length()) {
        val value = if (isNull(i)) null else opt(i)
        action(value)
    }
}

/**
 * 遍历 JSONArray 所有元素（带索引）。
 *
 * @param action 对每个元素执行的操作
 */
fun JSONArray.forEachIndexed(action: (Int, Any?) -> Unit) {
    for (i in 0 until length()) {
        val value = if (isNull(i)) null else opt(i)
        action(i, value)
    }
}

/**
 * 将 JSONArray 转换为 List。
 *
 * @return List<Any?>
 */
fun JSONArray.toList(): List<Any?> {
    val list = mutableListOf<Any?>()
    forEach { list.add(it) }
    return list
}

/**
 * 将 JSONObject 转换为 Map。
 *
 * @return Map<String, Any?>
 */
fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    forEach { key, value ->
        map[key] = when (value) {
            is JSONObject -> value.toMap()
            is JSONArray -> value.toList()
            else -> value
        }
    }
    return map
}

// endregion

// region 格式化

/**
 * 格式化 JSON 字符串（美化输出）。
 *
 * @param indentSpaces 缩进空格数
 * @return 格式化后的 JSON 字符串
 */
fun JSONObject.prettyPrint(indentSpaces: Int = 2): String {
    return toString(indentSpaces)
}

/**
 * 格式化 JSONArray 字符串（美化输出）。
 *
 * @param indentSpaces 缩进空格数
 * @return 格式化后的 JSON 字符串
 */
fun JSONArray.prettyPrint(indentSpaces: Int = 2): String {
    return toString(indentSpaces)
}

/**
 * 压缩 JSON 字符串（去除空白）。
 *
 * @return 压缩后的 JSON 字符串
 */
fun JSONObject.minify(): String {
    return toString()
}

/**
 * 压缩 JSONArray 字符串。
 *
 * @return 压缩后的 JSON 字符串
 */
fun JSONArray.minify(): String {
    return toString()
}

// endregion

// region 合并

/**
 * 合并另一个 JSONObject。
 *
 * @param other 要合并的 JSONObject
 * @param overwrite 是否覆盖已有键
 * @return 合并后的 JSONObject
 */
fun JSONObject.merge(other: JSONObject, overwrite: Boolean = true): JSONObject {
    val result = JSONObject(this.toString())
    other.forEach { key, value ->
        if (overwrite || !result.has(key)) {
            result.put(key, value)
        }
    }
    return result
}

/**
 * 深度合并 JSONObject。
 *
 * @param other 要合并的 JSONObject
 * @return 深度合并后的 JSONObject
 */
fun JSONObject.deepMerge(other: JSONObject): JSONObject {
    val result = JSONObject(this.toString())
    other.forEach { key, value ->
        if (value is JSONObject && result.has(key) && result.get(key) is JSONObject) {
            val existing = result.getJSONObject(key)
            result.put(key, existing.deepMerge(value))
        } else {
            result.put(key, value)
        }
    }
    return result
}

// endregion

// region 工具方法

/**
 * 检查是否包含所有键。
 *
 * @param keys 要检查的键列表
 * @return true 表示全部包含
 */
fun JSONObject.hasAll(vararg keys: String): Boolean {
    return keys.all { has(it) }
}

/**
 * 检查是否包含任意键。
 *
 * @param keys 要检查的键列表
 * @return true 表示至少包含一个
 */
fun JSONObject.hasAny(vararg keys: String): Boolean {
    return keys.any { has(it) }
}

/**
 * 移除指定键。
 *
 * @param keys 要移除的键列表
 * @return 修改后的 JSONObject
 */
fun JSONObject.remove(vararg keys: String): JSONObject {
    keys.forEach { remove(it) }
    return this
}

/**
 * 只保留指定键。
 *
 * @param keys 要保留的键列表
 * @return 修改后的 JSONObject
 */
fun JSONObject.keepOnly(vararg keys: String): JSONObject {
    val toRemove = mutableListOf<String>()
    forEach { key, _ ->
        if (key !in keys) {
            toRemove.add(key)
        }
    }
    toRemove.forEach { remove(it) }
    return this
}

/**
 * 获取所有键。
 *
 * @return 键列表
 */
fun JSONObject.keysList(): List<String> {
    val list = mutableListOf<String>()
    val keys = keys()
    while (keys.hasNext()) {
        list.add(keys.next())
    }
    return list
}

/**
 * 获取所有值。
 *
 * @return 值列表
 */
fun JSONObject.valuesList(): List<Any?> {
    val list = mutableListOf<Any?>()
    forEach { _, value -> list.add(value) }
    return list
}

/**
 * 键值对数量。
 */
fun JSONObject.size(): Int {
    return length()
}

/**
 * 是否为空。
 */
fun JSONObject.isEmpty(): Boolean {
    return length() == 0
}

/**
 * 是否不为空。
 */
fun JSONObject.isNotEmpty(): Boolean {
    return length() > 0
}

/**
 * JSONArray 大小。
 */
fun JSONArray.size(): Int {
    return length()
}

/**
 * JSONArray 是否为空。
 */
fun JSONArray.isEmpty(): Boolean {
    return length() == 0
}

/**
 * JSONArray 是否不为空。
 */
fun JSONArray.isNotEmpty(): Boolean {
    return length() > 0
}

/**
 * 从字符串安全解析为 JSONObject。
 *
 * @return JSONObject，解析失败返回空对象
 */
fun String?.toJsonObjectOrNull(): JSONObject? {
    return try {
        if (this.isNullOrBlank()) null else JSONObject(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * 从字符串安全解析为 JSONArray。
 *
 * @return JSONArray，解析失败返回空数组
 */
fun String?.toJsonArrayOrNull(): JSONArray? {
    return try {
        if (this.isNullOrBlank()) null else JSONArray(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * 从字符串解析为 JSONObject（默认空对象）。
 */
fun String?.toJsonObjectOrDefault(): JSONObject {
    return toJsonObjectOrNull() ?: JSONObject()
}

/**
 * 从字符串解析为 JSONArray（默认空数组）。
 */
fun String?.toJsonArrayOrDefault(): JSONArray {
    return toJsonArrayOrNull() ?: JSONArray()
}

// endregion
