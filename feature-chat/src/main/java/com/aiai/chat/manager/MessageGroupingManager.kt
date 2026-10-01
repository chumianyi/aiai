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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息分组管理器
 *
 * 管理聊天消息的日期分组显示。
 */
class MessageGroupingManager(private val context: Context) {

    data class GroupConfig(
        val enabled: Boolean = true,
        val showTimeDivider: Boolean = true,
        val groupByDay: Boolean = true
    )

    private val _config = MutableStateFlow(GroupConfig())
    val config: StateFlow<GroupConfig> = _config.asStateFlow()

    fun shouldShowDivider(currentTimestamp: Long, previousTimestamp: Long): Boolean {
        if (!_config.value.enabled) return false
        if (!_config.value.showTimeDivider) return false
        if (!_config.value.groupByDay) return true
        // 检查是否跨天
        val cal1 = java.util.Calendar.getInstance().apply { timeInMillis = currentTimestamp }
        val cal2 = java.util.Calendar.getInstance().apply { timeInMillis = previousTimestamp }
        return cal1.get(java.util.Calendar.DAY_OF_YEAR) != cal2.get(java.util.Calendar.DAY_OF_YEAR) ||
                cal1.get(java.util.Calendar.YEAR) != cal2.get(java.util.Calendar.YEAR)
    }

    fun updateConfig(config: GroupConfig) {
        _config.value = config
    }
}
