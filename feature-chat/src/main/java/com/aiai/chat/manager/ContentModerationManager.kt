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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息审核管理器
 *
 * 对发送和接收的消息进行内容安全审核。
 */
class ContentModerationManager(private val context: Context) {

    enum class RiskLevel {
        SAFE,
        LOW,
        MEDIUM,
        HIGH
    }

    data class ModerationResult(
        val isSafe: Boolean,
        val riskLevel: RiskLevel,
        val categories: List<String>,
        val suggestion: String = ""
    )

    private val _moderating = MutableStateFlow(false)
    val moderating: StateFlow<Boolean> = _moderating.asStateFlow()

    private val blockedKeywords = listOf("违规", "敏感词示例", "test_block")

    suspend fun moderateContent(content: String): ModerationResult {
        _moderating.value = true
        return try {
            val hasBlocked = blockedKeywords.any { content.contains(it, ignoreCase = true) }
            if (hasBlocked) {
                ModerationResult(
                    isSafe = false,
                    riskLevel = RiskLevel.HIGH,
                    categories = listOf("违规内容"),
                    suggestion = "内容包含敏感词，请修改后发送"
                )
            } else {
                ModerationResult(
                    isSafe = true,
                    riskLevel = RiskLevel.SAFE,
                    categories = emptyList()
                )
            }
        } finally {
            _moderating.value = false
        }
    }

    fun addBlockedKeyword(keyword: String) {
        // 动态添加敏感词（演示用）
    }
}
