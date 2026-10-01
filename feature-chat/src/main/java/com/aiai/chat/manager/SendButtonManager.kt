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
 * 发送按钮管理器
 *
 * 管理发送按钮的样式和行为。
 */
class SendButtonManager(private val context: Context) {

    data class SendButtonConfig(
        val enabled: Boolean = true,
        val cornerRadius: Float = 20f,
        val backgroundColor: Int = 0xFF667EEA.toInt(),
        val iconColor: Int = 0xFFFFFFFF.toInt(),
        val size: Int = 40
    )

    private val _config = MutableStateFlow(SendButtonConfig())
    val config: StateFlow<SendButtonConfig> = _config.asStateFlow()

    fun updateConfig(config: SendButtonConfig) {
        _config.value = config
    }

    fun setEnabled(enabled: Boolean) {
        _config.value = _config.value.copy(enabled = enabled)
    }

    fun isEnabled(): Boolean {
        return _config.value.enabled
    }
}
