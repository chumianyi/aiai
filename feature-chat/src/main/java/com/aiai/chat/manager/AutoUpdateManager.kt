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
 * 自动更新管理器
 *
 * 管理应用的自动更新检查。
 */
class AutoUpdateManager(private val context: Context) {

    data class AutoUpdateConfig(
        val enabled: Boolean = true,
        val checkIntervalHours: Int = 24,
        val wifiOnly: Boolean = true
    )

    private val _config = MutableStateFlow(AutoUpdateConfig())
    val config: StateFlow<AutoUpdateConfig> = _config.asStateFlow()

    private val _lastCheckTime = MutableStateFlow(0L)
    val lastCheckTime: StateFlow<Long> = _lastCheckTime.asStateFlow()

    fun shouldCheckUpdate(): Boolean {
        if (!_config.value.enabled) return false
        val intervalMs = _config.value.checkIntervalHours * 60 * 60 * 1000L
        return System.currentTimeMillis() - _lastCheckTime.value > intervalMs
    }

    fun onCheckCompleted() {
        _lastCheckTime.value = System.currentTimeMillis()
    }

    fun updateConfig(config: AutoUpdateConfig) {
        _config.value = config
    }
}
