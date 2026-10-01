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
import com.aiai.chat.data.model.Conversation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 会话统计管理器
 *
 * 统计用户的对话使用情况，包括消息数、token消耗、活跃天数等。
 */
class StatsManager(private val context: Context) {

    data class DailyStats(
        val date: String,
        val messageCount: Int,
        val tokenCount: Int,
        val conversationCount: Int
    )

    data class WeeklySummary(
        val totalMessages: Int,
        val totalTokens: Int,
        val activeDays: Int,
        val avgMessagesPerDay: Float,
        val topModel: String
    )

    private val _dailyStats = MutableStateFlow<List<DailyStats>>(emptyList())
    val dailyStats: StateFlow<List<DailyStats>> = _dailyStats.asStateFlow()

    private val _totalMessages = MutableStateFlow(0)
    val totalMessages: StateFlow<Int> = _totalMessages.asStateFlow()

    private val _totalTokens = MutableStateFlow(0)
    val totalTokens: StateFlow<Int> = _totalTokens.asStateFlow()

    private val _totalConversations = MutableStateFlow(0)
    val totalConversations: StateFlow<Int> = _totalConversations.asStateFlow()

    fun recordMessage(messageCount: Int = 1, tokenCount: Int = 0) {
        _totalMessages.value = _totalMessages.value + messageCount
        _totalTokens.value = _totalTokens.value + tokenCount
    }

    fun recordNewConversation() {
        _totalConversations.value = _totalConversations.value + 1
    }

    fun getWeeklySummary(): WeeklySummary {
        val weekStats = _dailyStats.value.takeLast(7)
        val totalMsgs = weekStats.sumOf { it.messageCount }
        val totalToks = weekStats.sumOf { it.tokenCount }
        val activeDays = weekStats.count { it.messageCount > 0 }
        val avg = if (activeDays > 0) totalMsgs.toFloat() / activeDays else 0f
        return WeeklySummary(
            totalMessages = totalMsgs,
            totalTokens = totalToks,
            activeDays = activeDays,
            avgMessagesPerDay = avg,
            topModel = "gpt-4o-mini"
        )
    }

    fun reset() {
        _totalMessages.value = 0
        _totalTokens.value = 0
        _totalConversations.value = 0
        _dailyStats.value = emptyList()
    }
}
