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
 * 对话分享响应。
 *
 * @property shareId 分享ID
 * @property shareUrl 分享链接
 * @property createdAt 创建时间
 * @property expiresAt 过期时间
 */
data class ShareResponse(
    @SerializedName("share_id")
    val shareId: String = "",

    @SerializedName("share_url")
    val shareUrl: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("expires_at")
    val expiresAt: String = "",
)

/**
 * 对话模板响应。
 *
 * @property id 模板ID
 * @property name 模板名称
 * @property description 描述
 * @property messages 模板消息列表
 * @property category 分类
 */
data class ConversationTemplateResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("messages")
    val messages: List<Map<String, String>> = emptyList(),

    @SerializedName("category")
    val category: String = "",
)

/**
 * 系统提示词响应。
 *
 * @property id 提示词ID
 * @property content 提示词内容
 * @property category 分类
 * @property isDefault 是否默认
 */
data class SystemPromptResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("category")
    val category: String = "",

    @SerializedName("is_default")
    val isDefault: Boolean = false,
)

/**
 * 模型能力响应。
 *
 * @property modelName 模型名称
 * @property capabilities 能力列表
 * @property maxContextLength 最大上下文长度
 * @property supportsStreaming 是否支持流式
 * @property supportsFunctionCalling 是否支持函数调用
 * @property supportsVision 是否支持视觉
 */
data class ModelCapabilitiesResponse(
    @SerializedName("model_name")
    val modelName: String = "",

    @SerializedName("capabilities")
    val capabilities: List<String> = emptyList(),

    @SerializedName("max_context_length")
    val maxContextLength: Int = 128000,

    @SerializedName("supports_streaming")
    val supportsStreaming: Boolean = true,

    @SerializedName("supports_function_calling")
    val supportsFunctionCalling: Boolean = false,

    @SerializedName("supports_vision")
    val supportsVision: Boolean = false,
)

/**
 * 速率限制状态响应。
 *
 * @property remainingRequests 剩余请求数
 * @property limit 总限制数
 * @property resetTime 重置时间戳
 * @property remainingTokens 剩余token数
 * @property tokenLimit token限制数
 */
data class RateLimitStatusResponse(
    @SerializedName("remaining_requests")
    val remainingRequests: Int = 0,

    @SerializedName("limit")
    val limit: Int = 0,

    @SerializedName("reset_time")
    val resetTime: Long = 0L,

    @SerializedName("remaining_tokens")
    val remainingTokens: Long = 0L,

    @SerializedName("token_limit")
    val tokenLimit: Long = 0L,
)

/**
 * 服务器公告响应。
 *
 * @property id 公告ID
 * @property title 标题
 * @property content 内容
 * @property type 类型
 * @property publishedAt 发布时间
 */
data class AnnouncementResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("type")
    val type: String = "info",

    @SerializedName("published_at")
    val publishedAt: String = "",
)
