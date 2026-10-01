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
 * 消息水印管理器
 *
 * 管理消息的水印显示。
 */
class WatermarkManager(private val context: Context) {

    data class WatermarkConfig(
        val enabled: Boolean = false,
        val text: String = "爱Ai",
        val opacity: Float = 0.1f,
        val size: Float = 14f,
        val rotation: Float = -30f
    )

    private val _config = MutableStateFlow(WatermarkConfig())
    val config: StateFlow<WatermarkConfig> = _config.asStateFlow()

    fun updateConfig(config: WatermarkConfig) {
        _config.value = config
    }

    fun enable() {
        _config.value = _config.value.copy(enabled = true)
    }

    fun disable() {
        _config.value = _config.value.copy(enabled = false)
    }

    fun isEnabled(): Boolean {
        return _config.value.enabled
    }
}
