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
 * 消息背景色管理器
 *
 * 管理聊天页面的整体背景色。
 */
class ChatBackgroundManager(private val context: Context) {

    data class BackgroundConfig(
        val backgroundColor: Int = 0xFFF5F5F5.toInt(),
        val useGradient: Boolean = false,
        val gradientStartColor: Int = 0xFF667EEA.toInt(),
        val gradientEndColor: Int = 0xFF764BA2.toInt()
    )

    private val _config = MutableStateFlow(BackgroundConfig())
    val config: StateFlow<BackgroundConfig> = _config.asStateFlow()

    fun updateConfig(config: BackgroundConfig) {
        _config.value = config
    }

    fun resetToDefault() {
        _config.value = BackgroundConfig()
    }
}
