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
package com.aiai.common.util.data

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON 工具类。
 *
 * 基于 org.json，提供 JSON 构建与解析。
 */
object JsonUtil {

    /** 构建 JSON 对象。 */
    fun buildJson(block: JSONObject.() -> Unit): String {
        val obj = JSONObject()
        block(obj)
        return obj.toString()
    }

    /** 构建 JSON 数组。 */
    fun buildJsonArray(block: JSONArray.() -> Unit): String {
        val arr = JSONArray()
        block(arr)
        return arr.toString()
    }

    /** 安全解析 JSON 对象。 */
    fun parseObject(json: String): JSONObject? {
        return try { JSONObject(json) } catch (e: Exception) { null }
    }

    /** 安全解析 JSON 数组。 */
    fun parseArray(json: String): JSONArray? {
        return try { JSONArray(json) } catch (e: Exception) { null }
    }
}
