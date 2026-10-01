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
package com.aiai.network.sse

import com.aiai.network.model.response.StreamResponse
import com.google.gson.Gson

/**
 * SSE数据解析器，将SSE事件数据解析为StreamResponse对象。
 *
 * 处理格式：
 * - data: {...json...}
 * - data: [DONE]
 */
class SseParser {

    private val gson = Gson()

    /**
     * 解析SSE事件数据。
     *
     * @param data SSE数据字符串
     * @return 解析结果，如果是[DONE]返回null
     */
    fun parse(data: String): StreamResponse? {
        if (data == "[DONE]") {
            return null
        }
        return try {
            gson.fromJson(data, StreamResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 判断是否为结束标记。
     *
     * @param data SSE数据
     * @return true如果是[DONE]
     */
    fun isDone(data: String): Boolean = data == "[DONE]"
}
