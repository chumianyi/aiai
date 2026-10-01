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
 * 消息空状态管理器
 *
 * 管理聊天列表为空时的空状态显示。
 */
class EmptyStateManager(private val context: Context) {

    data class EmptyStateConfig(
        val showEmptyState: Boolean = true,
        val emptyTitle: String = "开始新对话",
        val emptyDescription: String = "输入内容开始与AI助手聊天吧",
        val showActionButton: Boolean = true,
        val actionButtonText: String = "新建对话"
    )

    private val _config = MutableStateFlow(EmptyStateConfig())
    val config: StateFlow<EmptyStateConfig> = _config.asStateFlow()

    fun updateConfig(config: EmptyStateConfig) {
        _config.value = config
    }

    fun shouldShowEmptyState(messageCount: Int): Boolean {
        return _config.value.showEmptyState && messageCount == 0
    }
}
