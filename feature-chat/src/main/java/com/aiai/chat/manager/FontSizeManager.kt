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
 * 字体大小管理器
 *
 * 管理聊天消息字体大小的调节。
 */
class FontSizeManager(private val context: Context) {

    companion object {
        const val MIN_FONT_SIZE = 12f
        const val MAX_FONT_SIZE = 24f
        const val DEFAULT_FONT_SIZE = 15f
    }

    private val _fontSize = MutableStateFlow(DEFAULT_FONT_SIZE)
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()

    fun setFontSize(size: Float) {
        _fontSize.value = size.coerceIn(MIN_FONT_SIZE, MAX_FONT_SIZE)
    }

    fun increaseFontSize() {
        setFontSize(_fontSize.value + 1f)
    }

    fun decreaseFontSize() {
        setFontSize(_fontSize.value - 1f)
    }

    fun resetFontSize() {
        _fontSize.value = DEFAULT_FONT_SIZE
    }

    fun getFontSizePercentage(): Int {
        return ((_fontSize.value - MIN_FONT_SIZE) / (MAX_FONT_SIZE - MIN_FONT_SIZE) * 100).toInt()
    }
}
