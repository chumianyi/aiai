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
package com.aiai.chat.manager

import android.content.Context
import com.aiai.chat.data.model.ChatMessage
import com.aiai.chat.data.enums.MessageRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 上下文窗口管理器
 *
 * 管理发送给模型的上下文窗口大小，自动裁剪历史消息以适配token限制。
 */
class ContextWindowManager(private val context: Context) {

    data class ContextConfig(
        val maxContextTokens: Int = 4096,
        val reserveTokens: Int = 500,
        val includeSystemPrompt: Boolean = true,
        val maxHistoryMessages: Int = 20
    )

    private val _config = MutableStateFlow(ContextConfig())
    val config: StateFlow<ContextConfig> = _config.asStateFlow()

    /**
     * 构建发送给API的消息列表（自动裁剪上下文）
     */
    fun buildContextMessages(
        systemPrompt: String,
        history: List<ChatMessage>,
        newMessage: String
    ): List<Pair<String, String>> {
        val messages = mutableListOf<Pair<String, String>>()

        // 添加系统提示词
        if (_config.value.includeSystemPrompt && systemPrompt.isNotBlank()) {
            messages.add("system" to systemPrompt)
        }

        // 估算token并裁剪历史
        var totalTokens = systemPrompt.estimateTokens() + newMessage.estimateTokens()
        val reversed = history.reversed()

        for (msg in reversed) {
            val msgTokens = msg.content.estimateTokens()
            if (totalTokens + msgTokens > _config.value.maxContextTokens - _config.value.reserveTokens) {
                break
            }
            val role = when (msg.role) {
                MessageRole.USER -> "user"
                MessageRole.ASSISTANT -> "assistant"
                MessageRole.SYSTEM -> "system"
            }
            messages.add(role to msg.content)
            totalTokens += msgTokens
        }

        // 反转恢复顺序
        messages.reverse()

        // 添加新消息
        messages.add("user" to newMessage)

        return messages
    }

    fun updateConfig(config: ContextConfig) {
        _config.value = config
    }
}

private fun String.estimateTokens(): Int {
    val chineseChars = Regex("[\\u4e00-\\u9fa5]").findAll(this).count()
    val englishWords = Regex("[a-zA-Z]+").findAll(this).count()
    return (chineseChars * 1.5 + englishWords * 0.75).toInt()
}
