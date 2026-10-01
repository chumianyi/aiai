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
 * 停止生成按钮管理器
 *
 * 管理流式输出时的停止按钮显示。
 */
class StopGenerationManager(private val context: Context) {

    data class StopButtonConfig(
        val showStopButton: Boolean = true,
        val stopButtonText: String = "停止生成"
    )

    private val _config = MutableStateFlow(StopButtonConfig())
    val config: StateFlow<StopButtonConfig> = _config.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    fun onGenerationStart() {
        _isGenerating.value = true
    }

    fun onGenerationEnd() {
        _isGenerating.value = false
    }

    fun updateConfig(config: StopButtonConfig) {
        _config.value = config
    }

    fun shouldShowStopButton(): Boolean {
        return _config.value.showStopButton && _isGenerating.value
    }
}
