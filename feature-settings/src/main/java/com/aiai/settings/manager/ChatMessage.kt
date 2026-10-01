/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 对话消息模型（用于导出）。
 */
data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long
)

/**
 * 会话消息列表。
 */
object SampleMessages {

    fun forConversation(id: String): List<ChatMessage> = listOf(
        ChatMessage("user", "你好，请帮我写一段周报", System.currentTimeMillis() - 60000),
        ChatMessage("assistant", "好的，以下是本周周报：\n\n## 本周完成\n1. 完成设置模块开发\n2. 修复若干 bug\n\n## 下周计划\n1. 开始联调", System.currentTimeMillis() - 30000)
    )
}
