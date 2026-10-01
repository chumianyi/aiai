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
 * 暗色模式调度管理器
 *
 * 根据时间自动切换深色/浅色模式。
 */
class DarkModeScheduler(private val context: Context) {

    data class ScheduleConfig(
        val enabled: Boolean = false,
        val darkModeStartHour: Int = 22,
        val darkModeEndHour: Int = 7
    )

    private val _config = MutableStateFlow(ScheduleConfig())
    val config: StateFlow<ScheduleConfig> = _config.asStateFlow()

    private val _isDarkModeScheduled = MutableStateFlow(false)
    val isDarkModeScheduled: StateFlow<Boolean> = _isDarkModeScheduled.asStateFlow()

    fun checkAndUpdateDarkMode() {
        if (!_config.value.enabled) {
            _isDarkModeScheduled.value = false
            return
        }
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val start = _config.value.darkModeStartHour
        val end = _config.value.darkModeEndHour
        _isDarkModeScheduled.value = if (start > end) {
            hour >= start || hour < end
        } else {
            hour in start until end
        }
    }

    fun updateConfig(config: ScheduleConfig) {
        _config.value = config
        checkAndUpdateDarkMode()
    }
}
