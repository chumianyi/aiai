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
 * 屏幕常亮管理器
 *
 * 管理聊天时屏幕保持常亮功能。
 */
class KeepScreenOnManager(private val context: Context) {

    private val _keepScreenOn = MutableStateFlow(false)
    val keepScreenOn: StateFlow<Boolean> = _keepScreenOn.asStateFlow()

    fun enable() {
        _keepScreenOn.value = true
    }

    fun disable() {
        _keepScreenOn.value = false
    }

    fun toggle(): Boolean {
        _keepScreenOn.value = !_keepScreenOn.value
        return _keepScreenOn.value
    }

    fun isEnabled(): Boolean = _keepScreenOn.value
}
