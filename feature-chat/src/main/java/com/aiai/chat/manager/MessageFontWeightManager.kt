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
 * 消息字重管理器
 *
 * 管理聊天消息文字的字重。
 */
class MessageFontWeightManager(private val context: Context) {

    enum class FontWeight(val displayName: String, val weight: Int) {
        NORMAL("正常", 400),
        MEDIUM("中等", 500),
        BOLD("加粗", 700)
    }

    private val _fontWeight = MutableStateFlow(FontWeight.NORMAL)
    val fontWeight: StateFlow<FontWeight> = _fontWeight.asStateFlow()

    fun setFontWeight(weight: FontWeight) {
        _fontWeight.value = weight
    }

    fun getFontWeightInt(): Int {
        return _fontWeight.value.weight
    }
}
