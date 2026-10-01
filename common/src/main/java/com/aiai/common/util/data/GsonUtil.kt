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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Gson 工具类。
 *
 * 封装 Gson 单例，提供对象转 JSON / JSON 转对象。
 */
object GsonUtil {

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    /** 对象转 JSON 字符串。 */
    fun toJson(obj: Any): String = gson.toJson(obj)

    /** JSON 字符串转对象。 */
    inline fun <reified T> fromJson(json: String): T? {
        return try {
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    /** JSON 字符串转列表。 */
    inline fun <reified T> fromJsonList(json: String): List<T> {
        return try {
            gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
