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
package com.aiai.chat.data.model

/**
 * 流式响应数据模型
 *
 * 封装SSE流式输出的增量数据。
 *
 * @property deltaContent 增量文本内容
 * @property isFinished 是否结束
 * @property finishReason 结束原因
 * @property usageToken 使用的token数
 * @property error 错误信息
 */
data class StreamChunk(
    val deltaContent: String = "",
    val isFinished: Boolean = false,
    val finishReason: String? = null,
    val usageToken: Int = 0,
    val error: String? = null
) {
    companion object {
        /**
         * 从SSE数据行解析
         */
        fun fromSseData(data: String): StreamChunk? {
            return try {
                if (data == "[DONE]") {
                    StreamChunk(isFinished = true, finishReason = "stop")
                } else {
                    // 简化解析，实际项目使用Gson解析JSON
                    StreamChunk(deltaContent = data)
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
