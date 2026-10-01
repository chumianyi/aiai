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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON 工具类。
 */
object JsonUtil {

    /** 对象转 JSON。 */
    fun toJson(obj: Any): String {
        return when (obj) {
            is JSONObject -> obj.toString()
            is JSONArray -> obj.toString()
            else -> obj.toString()
        }
    }

    /** 解析为 JSONObject。 */
    fun parseObject(json: String): JSONObject? {
        return try {
            JSONObject(json)
        } catch (e: Exception) {
            null
        }
    }

    /** 解析为 JSONArray。 */
    fun parseArray(json: String): JSONArray? {
        return try {
            JSONArray(json)
        } catch (e: Exception) {
            null
        }
    }

    /** 安全获取字符串。 */
    fun optString(json: String, key: String, default: String = ""): String {
        return parseObject(json)?.optString(key, default) ?: default
    }

    /** 安全获取 Int。 */
    fun optInt(json: String, key: String, default: Int = 0): Int {
        return parseObject(json)?.optInt(key, default) ?: default
    }

    /** 安全获取 Boolean。 */
    fun optBoolean(json: String, key: String, default: Boolean = false): Boolean {
        return parseObject(json)?.optBoolean(key, default) ?: default
    }
}
