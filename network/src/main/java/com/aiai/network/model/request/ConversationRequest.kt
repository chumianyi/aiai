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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 会话请求模型。
 *
 * 用于创建、更新会话的API请求。
 *
 * @property title 会话标题
 * @property modelName 使用的模型名称
 * @property systemPrompt 系统提示词
 * @property temperature 温度参数
 * @property topP 核采样参数
 * @property maxTokens 最大token数
 * @property isPinned 是否置顶
 * @property isArchived 是否归档
 * @property conversationId 会话ID（更新时使用）
 */
data class ConversationRequest(
    @SerializedName("title")
    val title: String = "新对话",

    @SerializedName("model_name")
    val modelName: String = "gpt-4o",

    @SerializedName("system_prompt")
    val systemPrompt: String = "",

    @SerializedName("temperature")
    val temperature: Double = 0.7,

    @SerializedName("top_p")
    val topP: Double = 1.0,

    @SerializedName("max_tokens")
    val maxTokens: Int = 4096,

    @SerializedName("is_pinned")
    val isPinned: Boolean = false,

    @SerializedName("is_archived")
    val isArchived: Boolean = false,

    @SerializedName("conversation_id")
    val conversationId: String = "",
) {
    init {
        require(title.length <= 100) { "title must not exceed 100 characters" }
        require(temperature in 0.0..2.0) { "temperature must be between 0 and 2" }
        require(maxTokens > 0) { "max_tokens must be positive" }
    }
}
