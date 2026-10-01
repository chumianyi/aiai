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
 * 消息圆角管理器
 *
 * 管理聊天气泡的圆角大小。
 */
class BubbleRadiusManager(private val context: Context) {

    data class RadiusConfig(
        val topLeft: Float = 16f,
        val topRight: Float = 16f,
        val bottomLeft: Float = 16f,
        val bottomRight: Float = 16f
    )

    private val _userBubbleRadius = MutableStateFlow(RadiusConfig())
    val userBubbleRadius: StateFlow<RadiusConfig> = _userBubbleRadius.asStateFlow()

    private val _aiBubbleRadius = MutableStateFlow(RadiusConfig())
    val aiBubbleRadius: StateFlow<RadiusConfig> = _aiBubbleRadius.asStateFlow()

    fun setUserBubbleRadius(config: RadiusConfig) {
        _userBubbleRadius.value = config
    }

    fun setAiBubbleRadius(config: RadiusConfig) {
        _aiBubbleRadius.value = config
    }

    fun resetToDefault() {
        _userBubbleRadius.value = RadiusConfig()
        _aiBubbleRadius.value = RadiusConfig()
    }
}
