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
 * 消息边距管理器
 *
 * 管理聊天消息的边距和间距。
 */
class BubblePaddingManager(private val context: Context) {

    data class PaddingConfig(
        val horizontal: Int = 12,
        val vertical: Int = 8
    )

    private val _padding = MutableStateFlow(PaddingConfig())
    val padding: StateFlow<PaddingConfig> = _padding.asStateFlow()

    fun updatePadding(config: PaddingConfig) {
        _padding.value = config
    }

    fun resetToDefault() {
        _padding.value = PaddingConfig()
    }
}
