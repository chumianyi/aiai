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
 * 音效管理器
 *
 * 管理消息发送和接收的音效。
 */
class SoundEffectManager(private val context: Context) {

    enum class SoundType {
        SEND,
        RECEIVE,
        SUCCESS,
        ERROR,
        CLICK
    }

    private val _enabled = MutableStateFlow(true)
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    fun playSound(type: SoundType) {
        if (!_enabled.value) return
        // 播放音效
    }

    fun enable() {
        _enabled.value = true
    }

    fun disable() {
        _enabled.value = false
    }

    fun toggle(): Boolean {
        _enabled.value = !_enabled.value
        return _enabled.value
    }
}
