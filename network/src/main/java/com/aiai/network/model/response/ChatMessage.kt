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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 响应消息模型。
 *
 * @property role 角色（assistant）
 * @property content 消息内容
 * @property toolCalls 工具调用列表
 */
data class ChatMessage(
    @SerializedName("role")
    val role: String = "assistant",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("tool_calls")
    val toolCalls: List<ToolCallResponse>? = null,
)

/**
 * 工具调用响应模型。
 *
 * @property id 调用ID
 * @property type 调用类型
 * @property function 函数调用信息
 */
data class ToolCallResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("type")
    val type: String = "function",

    @SerializedName("function")
    val function: FunctionCallResponse = FunctionCallResponse(),
)

/**
 * 函数调用响应模型。
 *
 * @property name 函数名
 * @property arguments 函数参数
 */
data class FunctionCallResponse(
    @SerializedName("name")
    val name: String = "",

    @SerializedName("arguments")
    val arguments: String = "",
)
