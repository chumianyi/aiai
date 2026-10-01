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
 * 消息内容请求模型。
 *
 * 表示聊天中的单条消息，包含角色、内容、工具调用等。
 *
 * @property role 角色（system/user/assistant/tool）
 * @property content 消息内容
 * @property name 消息发送者名称（可选）
 * @property tool_calls 工具调用列表
 * @property tool_call_id 工具调用ID
 * @property attachments 附件列表
 */
data class MessageRequest(
    @SerializedName("role")
    val role: String = "user",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("tool_calls")
    val toolCalls: List<ToolCallRequest>? = null,

    @SerializedName("tool_call_id")
    val toolCallId: String? = null,

    @SerializedName("attachments")
    val attachments: List<AttachmentRequest>? = null,
) {
    init {
        require(role in listOf("system", "user", "assistant", "tool")) {
            "role must be one of: system, user, assistant, tool"
        }
    }

    companion object {
        /**
         * 创建系统消息。
         *
         * @param content 系统提示内容
         * @return 系统消息
         */
        fun system(content: String): MessageRequest = MessageRequest(
            role = "system",
            content = content,
        )

        /**
         * 创建用户消息。
         *
         * @param content 用户输入内容
         * @return 用户消息
         */
        fun user(content: String): MessageRequest = MessageRequest(
            role = "user",
            content = content,
        )

        /**
         * 创建助手消息。
         *
         * @param content 助手回复内容
         * @return 助手消息
         */
        fun assistant(content: String): MessageRequest = MessageRequest(
            role = "assistant",
            content = content,
        )

        /**
         * 创建工具消息。
         *
         * @param content 工具返回结果
         * @param toolCallId 工具调用ID
         * @return 工具消息
         */
        fun tool(content: String, toolCallId: String): MessageRequest = MessageRequest(
            role = "tool",
            content = content,
            toolCallId = toolCallId,
        )
    }
}

/**
 * 工具调用请求模型。
 *
 * @property id 调用ID
 * @property type 调用类型
 * @property function 函数调用
 */
data class ToolCallRequest(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("type")
    val type: String = "function",

    @SerializedName("function")
    val function: FunctionCallRequest = FunctionCallRequest(),
)

/**
 * 函数调用请求模型。
 *
 * @property name 函数名
 * @property arguments 函数参数（JSON字符串）
 */
data class FunctionCallRequest(
    @SerializedName("name")
    val name: String = "",

    @SerializedName("arguments")
    val arguments: String = "",
)

/**
 * 附件请求模型。
 *
 * @property type 附件类型（image/file/audio）
 * @property url 附件URL
 * @property fileName 文件名
 * @property mimeType MIME类型
 * @property size 文件大小（字节）
 */
data class AttachmentRequest(
    @SerializedName("type")
    val type: String = "file",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("file_name")
    val fileName: String = "",

    @SerializedName("mime_type")
    val mimeType: String = "",

    @SerializedName("size")
    val size: Long = 0L,
)
