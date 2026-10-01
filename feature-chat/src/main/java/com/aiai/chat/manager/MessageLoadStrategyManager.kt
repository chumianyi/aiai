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
 * 消息加载策略管理器
 *
 * 管理聊天消息的分页加载策略。
 */
class MessageLoadStrategyManager(private val context: Context) {

    data class LoadConfig(
        val pageSize: Int = 20,
        val prefetchDistance: Int = 5,
        val loadOlderThreshold: Int = 3
    )

    private val _config = MutableStateFlow(LoadConfig())
    val config: StateFlow<LoadConfig> = _config.asStateFlow()

    private val _isLoadingOlder = MutableStateFlow(false)
    val isLoadingOlder: StateFlow<Boolean> = _isLoadingOlder.asStateFlow()

    fun shouldLoadOlder(firstVisiblePosition: Int): Boolean {
        return firstVisiblePosition <= _config.value.loadOlderThreshold && !_isLoadingOlder.value
    }

    fun onLoadOlderStarted() {
        _isLoadingOlder.value = true
    }

    fun onLoadOlderFinished() {
        _isLoadingOlder.value = false
    }

    fun updateConfig(config: LoadConfig) {
        _config.value = config
    }
}
