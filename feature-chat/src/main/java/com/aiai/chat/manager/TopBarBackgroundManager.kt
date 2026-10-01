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
 * 顶部栏背景管理器
 *
 * 管理聊天页面顶部栏的背景样式。
 */
class TopBarBackgroundManager(private val context: Context) {

    data class TopBarBackgroundConfig(
        val backgroundColor: Int = 0xFFFFFFFF.toInt(),
        val elevation: Float = 4f,
        val showBottomBorder: Boolean = true,
        val bottomBorderColor: Int = 0xFFE0E0E0.toInt()
    )

    private val _config = MutableStateFlow(TopBarBackgroundConfig())
    val config: StateFlow<TopBarBackgroundConfig> = _config.asStateFlow()

    fun updateConfig(config: TopBarBackgroundConfig) {
        _config.value = config
    }
}
