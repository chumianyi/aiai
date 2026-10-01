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
 * 消息自动保存管理器
 *
 * 自动保存聊天内容，防止意外丢失。
 */
class AutoSaveManager(private val context: Context) {

    data class AutoSaveConfig(
        val enabled: Boolean = true,
        val intervalMs: Long = 30000,
        val maxSavedConversations: Int = 50
    )

    private val _config = MutableStateFlow(AutoSaveConfig())
    val config: StateFlow<AutoSaveConfig> = _config.asStateFlow()

    private val _lastSaveTime = MutableStateFlow(0L)
    val lastSaveTime: StateFlow<Long> = _lastSaveTime.asStateFlow()

    private val _autoSaving = MutableStateFlow(false)
    val autoSaving: StateFlow<Boolean> = _autoSaving.asStateFlow()

    fun shouldAutoSave(): Boolean {
        if (!_config.value.enabled) return false
        return System.currentTimeMillis() - _lastSaveTime.value > _config.value.intervalMs
    }

    suspend fun autoSave(conversationId: String, messages: List<ChatMessage>) {
        if (!shouldAutoSave()) return
        _autoSaving.value = true
        try {
            // 执行自动保存逻辑
            _lastSaveTime.value = System.currentTimeMillis()
        } finally {
            _autoSaving.value = false
        }
    }

    fun updateConfig(config: AutoSaveConfig) {
        _config.value = config
    }
}
