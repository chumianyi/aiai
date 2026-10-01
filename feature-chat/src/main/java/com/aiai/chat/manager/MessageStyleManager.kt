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
 * 消息卡片样式管理器
 *
 * 管理聊天消息卡片的整体样式主题。
 */
class MessageStyleManager(private val context: Context) {

    data class StyleConfig(
        val userBubbleColor: Int = 0xFF667EEA.toInt(),
        val aiBubbleColor: Int = 0xFFF5F5F5.toInt(),
        val userTextColor: Int = 0xFFFFFFFF.toInt(),
        val aiTextColor: Int = 0xFF333333.toInt(),
        val bubbleRadius: Float = 16f
    )

    private val _config = MutableStateFlow(StyleConfig())
    val config: StateFlow<StyleConfig> = _config.asStateFlow()

    fun updateConfig(config: StyleConfig) {
        _config.value = config
    }

    fun resetToDefault() {
        _config.value = StyleConfig()
    }

    fun getUserBubbleColor(): Int {
        return _config.value.userBubbleColor
    }

    fun getAiBubbleColor(): Int {
        return _config.value.aiBubbleColor
    }
}
