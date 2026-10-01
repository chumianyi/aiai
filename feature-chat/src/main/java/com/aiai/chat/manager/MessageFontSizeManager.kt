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
 * 消息字号管理器
 *
 * 管理聊天消息的字号大小设置。
 */
class MessageFontSizeManager(private val context: Context) {

    enum class FontSize(val displayName: String, val sizeSp: Float) {
        SMALL("小", 13f),
        MEDIUM("中", 15f),
        LARGE("大", 17f),
        EXTRA_LARGE("超大", 19f)
    }

    private val _fontSize = MutableStateFlow(FontSize.MEDIUM)
    val fontSize: StateFlow<FontSize> = _fontSize.asStateFlow()

    fun setFontSize(size: FontSize) {
        _fontSize.value = size
    }

    fun getFontSizeSp(): Float {
        return _fontSize.value.sizeSp
    }
}
