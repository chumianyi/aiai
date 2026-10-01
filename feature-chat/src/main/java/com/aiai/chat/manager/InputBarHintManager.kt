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
 * 输入框占位符管理器
 *
 * 管理底部输入框的占位提示文字。
 */
class InputBarHintManager(private val context: Context) {

    data class HintConfig(
        val hintText: String = "输入消息...",
        val hintColor: Int = 0xFF999999.toInt()
    )

    private val _config = MutableStateFlow(HintConfig())
    val config: StateFlow<HintConfig> = _config.asStateFlow()

    fun updateConfig(config: HintConfig) {
        _config.value = config
    }

    fun setHintText(text: String) {
        _config.value = _config.value.copy(hintText = text)
    }

    fun getHintText(): String {
        return _config.value.hintText
    }
}
