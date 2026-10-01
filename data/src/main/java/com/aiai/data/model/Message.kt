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
package com.aiai.data.model

/**
 * 消息领域模型。
 *
 * @property id 消息ID
 * @property conversationId 会话ID
 * @property role 角色
 * @property content 内容
 * @property messageType 消息类型
 * @property status 状态
 * @property createdAt 创建时间
 * @property tokenCount token数
 * @property modelName 模型名
 */
data class Message(
    val id: String,
    val conversationId: String,
    val role: String,
    val content: String,
    val messageType: String = "text",
    val status: String = "sent",
    val createdAt: Long = System.currentTimeMillis(),
    val tokenCount: Int = 0,
    val modelName: String = "gpt-4o",
) {
    fun isUser(): Boolean = role == "user"
    fun isAssistant(): Boolean = role == "assistant"
    fun isSystem(): Boolean = role == "system"
}
