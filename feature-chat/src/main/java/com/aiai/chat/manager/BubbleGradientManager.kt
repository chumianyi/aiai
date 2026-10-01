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
 * 消息渐变背景管理器
 *
 * 管理聊天气泡的渐变背景效果。
 */
class BubbleGradientManager(private val context: Context) {

    data class GradientConfig(
        val enabled: Boolean = false,
        val startColor: Int = 0xFF667EEA.toInt(),
        val endColor: Int = 0xFF764BA2.toInt(),
        val orientation: GradientOrientation = GradientOrientation.LEFT_RIGHT
    )

    enum class GradientOrientation {
        LEFT_RIGHT,
        TOP_BOTTOM,
        DIAGONAL
    }

    private val _config = MutableStateFlow(GradientConfig())
    val config: StateFlow<GradientConfig> = _config.asStateFlow()

    fun updateConfig(config: GradientConfig) {
        _config.value = config
    }

    fun isEnabled(): Boolean {
        return _config.value.enabled
    }
}
