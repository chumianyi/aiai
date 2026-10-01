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
 * 消息日期分隔符管理器
 *
 * 管理聊天列表中日期分隔符的显示。
 */
class DateDividerManager(private val context: Context) {

    data class DividerConfig(
        val showDivider: Boolean = true,
        val dividerTextSize: Float = 12f,
        val dividerPaddingVertical: Int = 8
    )

    private val _config = MutableStateFlow(DividerConfig())
    val config: StateFlow<DividerConfig> = _config.asStateFlow()

    fun shouldShowDivider(): Boolean {
        return _config.value.showDivider
    }

    fun updateConfig(config: DividerConfig) {
        _config.value = config
    }

    fun getDividerText(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 86_400_000 -> "今天"
            diff < 2 * 86_400_000 -> "昨天"
            diff < 7 * 86_400_000 -> "本周"
            else -> {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                sdf.format(java.util.Date(timestamp))
            }
        }
    }
}
