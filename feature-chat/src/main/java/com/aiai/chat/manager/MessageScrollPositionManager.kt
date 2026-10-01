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
 * 消息定位管理器
 *
 * 管理消息在列表中的滚动定位和位置记录。
 */
class MessageScrollPositionManager(private val context: Context) {

    private val _scrollPositions = MutableStateFlow<Map<String, Int>>(emptyMap())
    val scrollPositions: StateFlow<Map<String, Int>> = _scrollPositions.asStateFlow()

    fun savePosition(conversationId: String, position: Int) {
        _scrollPositions.value = _scrollPositions.value + (conversationId to position)
    }

    fun getPosition(conversationId: String): Int {
        return _scrollPositions.value[conversationId] ?: 0
    }

    fun clearPosition(conversationId: String) {
        _scrollPositions.value = _scrollPositions.value - conversationId
    }

    fun clearAll() {
        _scrollPositions.value = emptyMap()
    }
}
