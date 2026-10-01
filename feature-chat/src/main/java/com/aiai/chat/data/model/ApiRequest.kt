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
 * API请求配置模型
 *
 * 封装发送到AI模型API的请求参数。
 */
data class ApiRequest(
    val model: String,
    val messages: List<MessageItem>,
    val temperature: Double = 0.7,
    val maxTokens: Int = 2048,
    val topP: Double = 1.0,
    val frequencyPenalty: Double = 0.0,
    val presencePenalty: Double = 0.0,
    val stream: Boolean = true,
    val stop: String? = null,
    val user: String? = null
) {
    /**
     * 消息项
     */
    data class MessageItem(
        val role: String,
        val content: String
    )
}
