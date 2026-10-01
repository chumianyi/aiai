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
 * 聊天请求模型。
 *
 * 用于发送聊天消息到AI模型，支持配置模型参数、温度、流式输出等。
 *
 * @property model 模型名称，如 "gpt-4o"
 * @property messages 消息列表
 * @property temperature 采样温度，0-2，越高越随机
 * @property top_p 核采样参数，0-1
 * @property max_tokens 最大生成token数
 * @property stream 是否流式输出
 * @property presence_penalty 存在惩罚，-2.0到2.0
 * @property frequency_penalty 频率惩罚，-2.0到2.0
 * @property stop 停止词列表
 * @property conversationId 会话ID
 * @property userId 用户ID
 * @property tools 可用工具列表
 * @property tool_choice 工具选择策略
 */
data class ChatRequest(
    @SerializedName("model")
    val model: String = "gpt-4o",

    @SerializedName("messages")
    val messages: List<MessageRequest> = emptyList(),

    @SerializedName("temperature")
    val temperature: Double = 0.7,

    @SerializedName("top_p")
    val topP: Double = 1.0,

    @SerializedName("max_tokens")
    val maxTokens: Int = 4096,

    @SerializedName("stream")
    val stream: Boolean = false,

    @SerializedName("presence_penalty")
    val presencePenalty: Double = 0.0,

    @SerializedName("frequency_penalty")
    val frequencyPenalty: Double = 0.0,

    @SerializedName("stop")
    val stop: List<String>? = null,

    @SerializedName("conversation_id")
    val conversationId: String = "",

    @SerializedName("user_id")
    val userId: String = "",

    @SerializedName("tools")
    val tools: List<ToolRequest>? = null,

    @SerializedName("tool_choice")
    val toolChoice: String = "auto",
) {
    init {
        require(model.isNotBlank()) { "model must not be blank" }
        require(messages.isNotEmpty()) { "messages must not be empty" }
        require(temperature in 0.0..2.0) { "temperature must be between 0 and 2" }
        require(topP in 0.0..1.0) { "top_p must be between 0 and 1" }
        require(maxTokens > 0) { "max_tokens must be positive" }
    }

    /**
     * 构建Builder模式。
     *
     * @return ChatRequestBuilder
     */
    fun toBuilder(): Builder = Builder().apply {
        model = this@ChatRequest.model
        messages = this@ChatRequest.messages
        temperature = this@ChatRequest.temperature
        topP = this@ChatRequest.topP
        maxTokens = this@ChatRequest.maxTokens
        stream = this@ChatRequest.stream
    }

    /**
     * 聊天请求Builder。
     */
    class Builder {
        var model: String = "gpt-4o"
        var messages: List<MessageRequest> = emptyList()
        var temperature: Double = 0.7
        var topP: Double = 1.0
        var maxTokens: Int = 4096
        var stream: Boolean = false
        var presencePenalty: Double = 0.0
        var frequencyPenalty: Double = 0.0
        var stop: List<String>? = null
        var conversationId: String = ""
        var userId: String = ""
        var tools: List<ToolRequest>? = null
        var toolChoice: String = "auto"

        fun build(): ChatRequest = ChatRequest(
            model = model,
            messages = messages,
            temperature = temperature,
            topP = topP,
            maxTokens = maxTokens,
            stream = stream,
            presencePenalty = presencePenalty,
            frequencyPenalty = frequencyPenalty,
            stop = stop,
            conversationId = conversationId,
            userId = userId,
            tools = tools,
            toolChoice = toolChoice,
        )
    }
}

/**
 * 工具请求模型。
 *
 * @property type 工具类型
 * @property function 函数定义
 */
data class ToolRequest(
    @SerializedName("type")
    val type: String = "function",

    @SerializedName("function")
    val function: FunctionRequest = FunctionRequest(),
)

/**
 * 函数定义请求模型。
 *
 * @property name 函数名
 * @property description 函数描述
 * @property parameters 参数定义
 */
data class FunctionRequest(
    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("parameters")
    val parameters: Map<String, Any> = emptyMap(),
)
