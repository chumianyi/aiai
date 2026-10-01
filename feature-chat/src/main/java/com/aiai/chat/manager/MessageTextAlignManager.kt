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
 * 消息文字对齐管理器
 *
 * 管理聊天气泡中文字的对齐方式。
 */
class MessageTextAlignManager(private val context: Context) {

    enum class TextAlign(val displayName: String) {
        LEFT("左对齐"),
        CENTER("居中"),
        RIGHT("右对齐")
    }

    private val _textAlign = MutableStateFlow(TextAlign.LEFT)
    val textAlign: StateFlow<TextAlign> = _textAlign.asStateFlow()

    fun setTextAlign(align: TextAlign) {
        _textAlign.value = align
    }

    fun getTextAlign(): TextAlign {
        return _textAlign.value
    }
}
